package com.neology.parking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neology.parking.entity.Resident;

public interface ResidentRepository extends JpaRepository<Resident, String> {
}
