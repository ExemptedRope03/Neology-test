package com.neology.parking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.VehicleDetailResponse;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.dto.UpdateVehicleStatusRequest;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/neo/vehiculos")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/oficiales")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createOfficial(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request, VehicleType.OFFICIAL);
    }

    @PostMapping("/residentes")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createResident(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request, VehicleType.RESIDENT);
    }

    @PostMapping("/no-residentes")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createNonResident(@Valid @RequestBody CreateVehicleRequest request) {
        return vehicleService.create(request, VehicleType.NON_RESIDENT);
    }

    @GetMapping
    public List<VehicleResponse> list() {
        return vehicleService.list();
    }

    @PatchMapping("/{placa}")
    public VehicleResponse changeStatus(
            @PathVariable String placa,
            @Valid @RequestBody UpdateVehicleStatusRequest request) {
        return vehicleService.changeStatus(placa, request);
    }

    @GetMapping("/{placa}")
    public VehicleDetailResponse detail(@PathVariable String placa) {
        return vehicleService.detail(placa);
    }
}
