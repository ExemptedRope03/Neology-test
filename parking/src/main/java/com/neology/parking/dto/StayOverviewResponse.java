package com.neology.parking.dto;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StayOverviewResponse(
        Long estanciaId,
        String placa,
        VehicleType tipoVehiculo,
        LocalDateTime fechaHoraEntrada,
        LocalDateTime fechaHoraSalida,
        long minutos,
        BigDecimal costo,
        boolean pendiente) {
}
