package com.neology.parking.dto;

import com.neology.parking.entity.VehicleType;
import java.math.BigDecimal;

public record PaymentReportResponse(
        String placa,
        VehicleType tipo,
        long minutosAcumulados,
        BigDecimal importe) {
}
