package com.neology.parking.service;

public interface MonthService {
    MonthResetResult reset();

    record MonthResetResult(long estanciasEliminadas, int residentesReiniciados) { }
}
