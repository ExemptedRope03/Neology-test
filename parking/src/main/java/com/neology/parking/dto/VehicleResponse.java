package com.neology.parking.dto;

import com.neology.parking.entity.VehicleType;

public record VehicleResponse(String placa, VehicleType tipo) {
}
