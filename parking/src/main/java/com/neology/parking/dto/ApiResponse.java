package com.neology.parking.dto;

import java.time.Instant;

/** Contrato uniforme de salida para toda la API. */
public record ApiResponse<T>(Instant timestamp, int status, T data, boolean succes) {

    public static <T> ApiResponse<T> success(int status, T data) {
        return new ApiResponse<>(Instant.now(), status, data, true);
    }

    public static ApiResponse<String> failure(int status, String message) {
        return new ApiResponse<>(Instant.now(), status, message, false);
    }
}
