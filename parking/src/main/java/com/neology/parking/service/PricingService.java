package com.neology.parking.service;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;

public interface PricingService {
    BigDecimal calculate(VehicleType type, long minutes);
}
