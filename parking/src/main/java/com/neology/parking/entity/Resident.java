package com.neology.parking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "residents")
public class Resident {
    @Id
    @Column(length = 20, nullable = false, updatable = false)
    private String plate;

    @Column(nullable = false)
    private long accumulatedMinutes;

    protected Resident() { }

    public Resident(String plate) { this.plate = plate; }
    public String getPlate() { return plate; }
    public long getAccumulatedMinutes() { return accumulatedMinutes; }
    public void addMinutes(long minutes) { this.accumulatedMinutes += minutes; }
    public void resetAccumulatedMinutes() { this.accumulatedMinutes = 0; }
}
