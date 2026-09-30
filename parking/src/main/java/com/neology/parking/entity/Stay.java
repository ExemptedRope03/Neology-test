package com.neology.parking.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "stays", indexes = @Index(name = "idx_stay_plate", columnList = "plate"))
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String plate;

    @Column(nullable = false)
    private LocalDateTime entryAt;

    private LocalDateTime exitAt;

    protected Stay() {
    }

    public Stay(String plate, LocalDateTime entryAt) {
        this.plate = plate;
        this.entryAt = entryAt;
    }

    public Long getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public LocalDateTime getEntryAt() {
        return entryAt;
    }

    public LocalDateTime getExitAt() {
        return exitAt;
    }

    public void registerExit(LocalDateTime exitAt) {
        this.exitAt = exitAt;
    }
}
