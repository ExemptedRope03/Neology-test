package com.neology.parking.controller;

import com.neology.parking.dto.EntryResponse;
import com.neology.parking.dto.ExitResponse;
import com.neology.parking.dto.StayRequest;
import com.neology.parking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/estancias")
public class StayController {
    private final ParkingService parkingService;

    public StayController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entrada")
    public ResponseEntity<EntryResponse> entry(@Valid @RequestBody StayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.registerEntry(request));
    }

    @PostMapping("/salida")
    public ExitResponse exit(@Valid @RequestBody StayRequest request) {
        return parkingService.registerExit(request);
    }
}
