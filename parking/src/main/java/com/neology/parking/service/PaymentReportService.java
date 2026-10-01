package com.neology.parking.service;

import com.neology.parking.dto.PaymentReportResponse;
import com.neology.parking.entity.VehicleType;
import java.util.List;

public interface PaymentReportService {
    List<PaymentReportResponse> report(VehicleType type);
}
