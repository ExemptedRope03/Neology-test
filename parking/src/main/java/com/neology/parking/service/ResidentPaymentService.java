package com.neology.parking.service;

import com.neology.parking.dto.ResidentPaymentResponse;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.repository.ResidentRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResidentPaymentService {
    private final ResidentRepository residentRepository;
    private final PricingService pricingService;

    public ResidentPaymentService(ResidentRepository residentRepository, PricingService pricingService) {
        this.residentRepository = residentRepository;
        this.pricingService = pricingService;
    }

    @Transactional(readOnly = true)
    public List<ResidentPaymentResponse> report() {
        return residentRepository.findAll().stream()
                .sorted(Comparator.comparing(Resident::getPlate))
                .map(resident -> new ResidentPaymentResponse(
                        resident.getPlate(),
                        resident.getAccumulatedMinutes(),
                        pricingService.calculate(VehicleType.RESIDENT, resident.getAccumulatedMinutes())))
                .toList();
    }
}
