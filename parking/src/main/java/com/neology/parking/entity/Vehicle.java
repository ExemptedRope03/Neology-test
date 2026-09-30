package com.neology.parking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @Column(length = 20, nullable = false, updatable = false)
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VehicleType type;

    protected Vehicle() { }

    public Vehicle(String plate, VehicleType type) {
        this.plate = plate;
        this.type = type;
    }

    public String getPlate() { return plate; }
    public VehicleType getType() { return type; }
}
