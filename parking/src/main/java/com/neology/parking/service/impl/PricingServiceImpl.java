package com.neology.parking.service.impl;

import com.neology.parking.entity.VehicleType;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.service.PricingPolicy;
import com.neology.parking.service.PricingService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {
    private final List<PricingPolicy> policies;

    public PricingServiceImpl(List<PricingPolicy> policies) {
        this.policies = List.copyOf(policies);
    }

    @Override
    public BigDecimal calculate(VehicleType type, long minutes) {
        if (minutes < 0) {
            throw new BusinessRuleException("Los minutos no pueden ser negativos");
        }
        return policies.stream()
                .filter(policy -> policy.supports(type))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("No hay política de cobro para " + type))
                .calculate(minutes);
    }
}
