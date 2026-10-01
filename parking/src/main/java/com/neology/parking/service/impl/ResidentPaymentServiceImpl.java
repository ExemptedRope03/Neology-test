package com.neology.parking.service.impl;

import com.neology.parking.dto.ResidentPaymentResponse;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.service.PricingService;
import com.neology.parking.service.ResidentPaymentService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResidentPaymentServiceImpl implements ResidentPaymentService {
    private final ResidentRepository residentRepository;
    private final PricingService pricingService;

    public ResidentPaymentServiceImpl(ResidentRepository residentRepository, PricingService pricingService) {
        this.residentRepository = residentRepository;
        this.pricingService = pricingService;
    }

    @Override
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
