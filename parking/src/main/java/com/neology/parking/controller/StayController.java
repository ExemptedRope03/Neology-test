package com.neology.parking.controller;

import java.util.List;

import com.neology.parking.dto.EntryResponse;
import com.neology.parking.dto.ExitResponse;
import com.neology.parking.dto.StayOverviewResponse;
import com.neology.parking.dto.StayRequest;
import com.neology.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/estancias")
public class StayController {
    private final ParkingService parkingService;

    public StayController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entrada")
    @ResponseStatus(HttpStatus.CREATED)
    public EntryResponse entry(@Valid @RequestBody StayRequest request) {
        return parkingService.registerEntry(request);
    }

    @PostMapping("/salida")
    public ExitResponse exit(@Valid @RequestBody StayRequest request) {
        return parkingService.registerExit(request);
    }

    @GetMapping
    public List<StayOverviewResponse> list() {
        return parkingService.listStays();
    }
}
