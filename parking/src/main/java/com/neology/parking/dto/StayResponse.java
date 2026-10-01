package com.neology.parking.dto;

import java.time.LocalDateTime;

public record StayResponse(Long id, LocalDateTime fechaHoraEntrada, LocalDateTime fechaHoraSalida) {
}
