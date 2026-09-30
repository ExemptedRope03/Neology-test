package com.neology.parking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExitResponse(
        Long estanciaId,
        String placa,
        LocalDateTime fechaHoraSalida,
        long minutos,
        BigDecimal importeACobrar) {
}
