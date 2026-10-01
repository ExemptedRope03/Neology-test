package com.neology.parking.service.impl;

import com.neology.parking.dto.EntryResponse;
import com.neology.parking.dto.ExitResponse;
import com.neology.parking.dto.StayOverviewResponse;
import com.neology.parking.dto.StayRequest;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Stay;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleStatus;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.exception.ResourceNotFoundException;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import com.neology.parking.service.ParkingService;
import com.neology.parking.service.PricingService;
import com.neology.parking.service.VehicleService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingServiceImpl implements ParkingService {
    private final VehicleRepository vehicleRepository;
    private final StayRepository stayRepository;
    private final ResidentRepository residentRepository;
    private final PricingService pricingService;
    private final Clock clock;

    public ParkingServiceImpl(VehicleRepository vehicleRepository, StayRepository stayRepository,
            ResidentRepository residentRepository, PricingService pricingService, Clock clock) {
        this.vehicleRepository = vehicleRepository;
        this.stayRepository = stayRepository;
        this.residentRepository = residentRepository;
        this.pricingService = pricingService;
        this.clock = clock;
    }

    @Override
    @Transactional
    public EntryResponse registerEntry(StayRequest request) {
        String plate = VehicleService.normalize(request.placa());
        Vehicle vehicle = vehicle(plate);
        if (vehicle.getStatus() == VehicleStatus.INACTIVE) {
            throw new BusinessRuleException("El vehículo con placa " + plate + " está inactivo");
        }
        if (stayRepository.findByPlateAndExitAtIsNull(plate).isPresent()) {
            throw new BusinessRuleException("El vehículo ya tiene una estancia abierta");
        }
        Stay stay = stayRepository.save(new Stay(plate, LocalDateTime.now(clock)));
        return new EntryResponse(stay.getId(), plate, stay.getEntryAt());
    }

    @Override
    @Transactional
    public ExitResponse registerExit(StayRequest request) {
        String plate = VehicleService.normalize(request.placa());
        Vehicle vehicle = vehicle(plate);
        Stay stay = stayRepository.findByPlateAndExitAtIsNull(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una estancia abierta para " + plate));
        LocalDateTime exitAt = LocalDateTime.now(clock);
        long minutes = Duration.between(stay.getEntryAt(), exitAt).toMinutes();
        if (minutes < 0) {
            throw new BusinessRuleException("La salida no puede ser anterior a la entrada");
        }
        stay.registerExit(exitAt);
        stayRepository.save(stay);

        BigDecimal amount = vehicle.getType() == VehicleType.RESIDENT
                ? registerResidentMinutes(plate, minutes)
                : pricingService.calculate(vehicle.getType(), minutes);
        return new ExitResponse(stay.getId(), plate, exitAt, minutes, amount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StayOverviewResponse> listStays() {
        LocalDateTime now = LocalDateTime.now(clock);
        Map<String, Vehicle> vehiclesByPlate = vehicleRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(Vehicle::getPlate, Function.identity()));

        return stayRepository.findAllByOrderByEntryAtDesc().stream()
                .map(stay -> overview(stay, vehiclesByPlate.get(stay.getPlate()), now))
                .toList();
    }

    private Vehicle vehicle(String plate) {
        return vehicleRepository.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el vehículo con placa " + plate));
    }

    private BigDecimal registerResidentMinutes(String plate, long minutes) {
        Resident resident = residentRepository.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el residente con placa " + plate));
        resident.addMinutes(minutes);
        residentRepository.save(resident);
        return BigDecimal.ZERO;
    }

    private StayOverviewResponse overview(Stay stay, Vehicle vehicle, LocalDateTime now) {
        LocalDateTime endAt = stay.getExitAt() == null ? now : stay.getExitAt();
        long minutes = Math.max(0, Duration.between(stay.getEntryAt(), endAt).toMinutes());
        VehicleType type = vehicle == null ? null : vehicle.getType();
        BigDecimal amount = type == null ? BigDecimal.ZERO : pricingService.calculate(type, minutes);
        return new StayOverviewResponse(
                stay.getId(),
                stay.getPlate(),
                type,
                stay.getEntryAt(),
                stay.getExitAt(),
                minutes,
                amount,
                stay.getExitAt() == null);
    }
}
