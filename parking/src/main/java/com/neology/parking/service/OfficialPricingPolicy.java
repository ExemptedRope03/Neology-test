package com.neology.parking.service;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class OfficialPricingPolicy implements PricingPolicy {
    public boolean supports(VehicleType type) { return type == VehicleType.OFFICIAL; }
    public BigDecimal calculate(long minutes) { return BigDecimal.ZERO; }
}
