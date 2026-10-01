package com.neology.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateVehicleRequest(
        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 20, message = "La placa no puede exceder 20 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9-]{3,20}$", message = "La placa solo acepta letras, números y guiones")
        String placa) {
}
