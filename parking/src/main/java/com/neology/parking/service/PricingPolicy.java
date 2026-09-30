package com.neology.parking.service;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;

/** Permite agregar nuevos tipos mediante una política registrada como bean. */
public interface PricingPolicy {
    boolean supports(VehicleType type);
    BigDecimal calculate(long minutes);
}
