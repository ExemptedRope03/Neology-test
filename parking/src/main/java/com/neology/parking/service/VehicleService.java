package com.neology.parking.service;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ResidentRepository residentRepository;

    public VehicleService(VehicleRepository vehicleRepository, ResidentRepository residentRepository) {
        this.vehicleRepository = vehicleRepository;
        this.residentRepository = residentRepository;
    }

    @Transactional
    public VehicleResponse create(CreateVehicleRequest request, VehicleType type) {
        String plate = normalize(request.placa());
        if (vehicleRepository.existsById(plate)) {
            throw new BusinessRuleException("Ya existe un vehículo con placa " + plate);
        }
        Vehicle vehicle = vehicleRepository.save(new Vehicle(plate, type));
        if (type == VehicleType.RESIDENT) {
            residentRepository.save(new Resident(plate));
        }
        return new VehicleResponse(vehicle.getPlate(), vehicle.getType());
    }

    static String normalize(String plate) {
        return plate.trim().toUpperCase();
    }
}
