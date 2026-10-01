package com.neology.parking.service;

import java.util.List;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.VehicleDetailResponse;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.dto.UpdateVehicleStatusRequest;
import com.neology.parking.entity.VehicleType;

public interface VehicleService {
    VehicleResponse create(CreateVehicleRequest request, VehicleType type);
    List<VehicleResponse> list();
    VehicleDetailResponse detail(String plate);
    VehicleResponse changeStatus(String plate, UpdateVehicleStatusRequest request);

    static String normalize(String plate) {
        return plate.trim().toUpperCase();
    }
}
