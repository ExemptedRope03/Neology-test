package com.neology.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StayRequest(
        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 20, message = "La placa no puede exceder 20 caracteres")
        String placa) {
}
