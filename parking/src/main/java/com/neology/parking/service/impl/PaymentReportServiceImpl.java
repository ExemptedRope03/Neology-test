package com.neology.parking.service.impl;

import com.neology.parking.dto.PaymentReportResponse;
import com.neology.parking.dto.StayOverviewResponse;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.VehicleRepository;
import com.neology.parking.service.ParkingService;
import com.neology.parking.service.PaymentReportService;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentReportServiceImpl implements PaymentReportService {
    private final VehicleRepository vehicleRepository;
    private final ResidentRepository residentRepository;
    private final ParkingService parkingService;

    public PaymentReportServiceImpl(VehicleRepository vehicleRepository,
            ResidentRepository residentRepository, ParkingService parkingService) {
        this.vehicleRepository = vehicleRepository;
        this.residentRepository = residentRepository;
        this.parkingService = parkingService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentReportResponse> report(VehicleType type) {
        Map<String, Resident> residents = residentRepository.findAll().stream()
                .collect(Collectors.toMap(Resident::getPlate, resident -> resident));
        Map<String, List<StayOverviewResponse>> staysByPlate = parkingService.listStays().stream()
                .collect(Collectors.groupingBy(StayOverviewResponse::placa));

        return vehicleRepository.findAll().stream()
                .filter(vehicle -> type == null || vehicle.getType() == type)
                .map(vehicle -> reportFor(vehicle, residents.get(vehicle.getPlate()),
                        staysByPlate.getOrDefault(vehicle.getPlate(), List.of())))
                .sorted(Comparator.comparing(PaymentReportResponse::placa))
                .toList();
    }

    private PaymentReportResponse reportFor(Vehicle vehicle, Resident resident,
            List<StayOverviewResponse> stays) {
        if (vehicle.getType() == VehicleType.RESIDENT) {
            long minutes = resident == null ? 0 : resident.getAccumulatedMinutes();
            return new PaymentReportResponse(
                    vehicle.getPlate(), vehicle.getType(), minutes,
                    BigDecimal.valueOf(minutes).multiply(new BigDecimal("0.05")));
        }

        long minutes = stays.stream().mapToLong(StayOverviewResponse::minutos).sum();
        BigDecimal amount = stays.stream()
                .map(StayOverviewResponse::costo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PaymentReportResponse(vehicle.getPlate(), vehicle.getType(), minutes, amount);
    }
}
