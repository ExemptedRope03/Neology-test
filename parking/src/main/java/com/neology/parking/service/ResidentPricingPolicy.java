package com.neology.parking.service;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ResidentPricingPolicy implements PricingPolicy {
    private static final BigDecimal RATE_PER_MINUTE = new BigDecimal("0.05");
    public boolean supports(VehicleType type) { return type == VehicleType.RESIDENT; }
    public BigDecimal calculate(long minutes) { return RATE_PER_MINUTE.multiply(BigDecimal.valueOf(minutes)); }
}
