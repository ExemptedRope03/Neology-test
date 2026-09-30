package com.neology.parking.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.neology.parking.entity.VehicleType;
import java.util.List;
import org.junit.jupiter.api.Test;

class PricingServiceTest {
    private final PricingService pricingService = new PricingService(List.of(
            new OfficialPricingPolicy(), new ResidentPricingPolicy(), new NonResidentPricingPolicy()));

    @Test
    void calculatesRatesForEveryVehicleType() {
        assertThat(pricingService.calculate(VehicleType.OFFICIAL, 100)).isEqualByComparingTo("0");
        assertThat(pricingService.calculate(VehicleType.RESIDENT, 100)).isEqualByComparingTo("5.00");
        assertThat(pricingService.calculate(VehicleType.NON_RESIDENT, 7)).isEqualByComparingTo("3.50");
    }
}
