package com.neology.parking.service.impl;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.StayResponse;
import com.neology.parking.dto.VehicleDetailResponse;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.dto.UpdateVehicleStatusRequest;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.exception.ResourceNotFoundException;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import com.neology.parking.service.PricingService;
import com.neology.parking.service.VehicleService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ResidentRepository residentRepository;
    private final StayRepository stayRepository;
    private final PricingService pricingService;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, ResidentRepository residentRepository,
            StayRepository stayRepository, PricingService pricingService) {
        this.vehicleRepository = vehicleRepository;
        this.residentRepository = residentRepository;
        this.stayRepository = stayRepository;
        this.pricingService = pricingService;
    }

    @Override
    @Transactional
    public VehicleResponse create(CreateVehicleRequest request, VehicleType type) {
        String plate = VehicleService.normalize(request.placa());
        if (vehicleRepository.existsById(plate)) {
            throw new BusinessRuleException("Ya existe un vehículo con placa " + plate);
        }
        Vehicle vehicle = vehicleRepository.save(new Vehicle(plate, type));
        if (type == VehicleType.RESIDENT) {
            residentRepository.save(new Resident(plate));
        }
        return new VehicleResponse(vehicle.getPlate(), vehicle.getType(), vehicle.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> list() {
        return vehicleRepository.findAll().stream()
                .sorted(Comparator.comparing(Vehicle::getPlate))
                .map(vehicle -> new VehicleResponse(vehicle.getPlate(), vehicle.getType(), vehicle.getStatus()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDetailResponse detail(String rawPlate) {
        String plate = VehicleService.normalize(rawPlate);
        Vehicle vehicle = vehicleRepository.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el vehículo con placa " + plate));
        List<StayResponse> stays = stayRepository.findByPlateOrderByEntryAtDesc(plate).stream()
                .map(stay -> new StayResponse(stay.getId(), stay.getEntryAt(), stay.getExitAt()))
                .toList();

        if (vehicle.getType() != VehicleType.RESIDENT) {
            return new VehicleDetailResponse(plate, vehicle.getType(), vehicle.getStatus(), stays, null, null);
        }
        Resident resident = residentRepository.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el residente con placa " + plate));
        long minutes = resident.getAccumulatedMinutes();
        return new VehicleDetailResponse(plate, vehicle.getType(), vehicle.getStatus(), stays, minutes,
                pricingService.calculate(VehicleType.RESIDENT, minutes));
    }

    @Override
    @Transactional
    public VehicleResponse changeStatus(String rawPlate, UpdateVehicleStatusRequest request) {
        String plate = VehicleService.normalize(rawPlate);
        Vehicle vehicle = vehicleRepository.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el vehículo con placa " + plate));
        vehicle.setStatus(request.status());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return new VehicleResponse(savedVehicle.getPlate(), savedVehicle.getType(), savedVehicle.getStatus());
    }
}
