package com.neology.parking.repository;

import com.neology.parking.entity.Stay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRepository extends JpaRepository<Stay, Long> {
    Optional<Stay> findByPlateAndExitAtIsNull(String plate);
    List<Stay> findByPlateOrderByEntryAtDesc(String plate);
    List<Stay> findAllByOrderByEntryAtDesc();
}
