package com.neology.parking.dto;

import com.neology.parking.entity.VehicleStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleStatusRequest(
        @NotNull(message = "El estado es obligatorio")
        VehicleStatus status) {
}
