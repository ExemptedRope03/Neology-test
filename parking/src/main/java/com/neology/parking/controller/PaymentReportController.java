package com.neology.parking.controller;

import com.neology.parking.dto.PaymentReportResponse;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.service.PaymentReportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/reportes/pagos")
public class PaymentReportController {
    private final PaymentReportService paymentReportService;

    public PaymentReportController(PaymentReportService paymentReportService) {
        this.paymentReportService = paymentReportService;
    }

    @GetMapping
    public List<PaymentReportResponse> report(
            @RequestParam(required = false) VehicleType tipo) {
        return paymentReportService.report(tipo);
    }
}
