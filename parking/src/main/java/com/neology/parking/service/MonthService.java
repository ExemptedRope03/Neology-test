package com.neology.parking.service;

import com.neology.parking.entity.Resident;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonthService {
    private final StayRepository stayRepository;
    private final ResidentRepository residentRepository;

    public MonthService(StayRepository stayRepository, ResidentRepository residentRepository) {
        this.stayRepository = stayRepository;
        this.residentRepository = residentRepository;
    }

    /** El catálogo de vehículos permanece; se borran estancias y tiempos del período. */
    @Transactional
    public MonthResetResult reset() {
        long staysRemoved = stayRepository.count();
        stayRepository.deleteAllInBatch();
        var residents = residentRepository.findAll();
        residents.forEach(Resident::resetAccumulatedMinutes);
        residentRepository.saveAll(residents);
        return new MonthResetResult(staysRemoved, residents.size());
    }

    public record MonthResetResult(long estanciasEliminadas, int residentesReiniciados) { }
}
