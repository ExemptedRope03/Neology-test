package com.neology.parking.controller;

import com.neology.parking.dto.ResidentPaymentResponse;
import com.neology.parking.service.ResidentPaymentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/residentes")
public class ResidentController {
    private final ResidentPaymentService paymentService;

    public ResidentController(ResidentPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/pagos")
    public List<ResidentPaymentResponse> payments() {
        return paymentService.report();
    }
}
