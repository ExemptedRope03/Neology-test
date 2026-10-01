package com.neology.parking.dto;

import com.neology.parking.entity.VehicleType;
import com.neology.parking.entity.VehicleStatus;
import java.math.BigDecimal;
import java.util.List;

public record VehicleDetailResponse(
        String placa,
        VehicleType tipo,
        VehicleStatus status,
        List<StayResponse> estancias,
        Long minutosAcumulados,
        BigDecimal pagoAcumulado) {
}
