package com.neology.parking.controller;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo/vehiculos")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/oficiales")
    public ResponseEntity<VehicleResponse> createOfficial(@Valid @RequestBody CreateVehicleRequest request) {
        return create(request, VehicleType.OFFICIAL);
    }

    @PostMapping("/residentes")
    public ResponseEntity<VehicleResponse> createResident(@Valid @RequestBody CreateVehicleRequest request) {
        return create(request, VehicleType.RESIDENT);
    }

    @PostMapping("/no-residentes")
    public ResponseEntity<VehicleResponse> createNonResident(@Valid @RequestBody CreateVehicleRequest request) {
        return create(request, VehicleType.NON_RESIDENT);
    }

    private ResponseEntity<VehicleResponse> create(CreateVehicleRequest request, VehicleType type) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(request, type));
    }
}
