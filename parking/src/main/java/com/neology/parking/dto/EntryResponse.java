package com.neology.parking.dto;

import java.time.LocalDateTime;

public record EntryResponse(Long estanciaId, String placa, LocalDateTime fechaHoraEntrada) {
}
