package com.neology.parking.dto;

import com.neology.parking.entity.VehicleType;
import com.neology.parking.entity.VehicleStatus;

public record VehicleResponse(String placa, VehicleType tipo, VehicleStatus status) {
}
