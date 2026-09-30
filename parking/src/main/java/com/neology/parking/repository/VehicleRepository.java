package com.neology.parking.repository;

import com.neology.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
