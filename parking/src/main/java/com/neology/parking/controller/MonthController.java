package com.neology.parking.controller;

import com.neology.parking.dto.MonthResetResponse;
import com.neology.parking.service.MonthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/mes")
public class MonthController {
    private final MonthService monthService;

    public MonthController(MonthService monthService) {
        this.monthService = monthService;
    }

    @PostMapping("/iniciar")
    public MonthResetResponse start() {
        MonthService.MonthResetResult result = monthService.reset();
        return new MonthResetResponse(result.estanciasEliminadas(), result.residentesReiniciados());
    }
}
