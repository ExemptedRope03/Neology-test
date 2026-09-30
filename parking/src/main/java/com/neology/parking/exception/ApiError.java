package com.neology.parking.exception;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, int status, String error) {
}
