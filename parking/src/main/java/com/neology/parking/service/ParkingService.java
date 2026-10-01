package com.neology.parking.service;

import com.neology.parking.dto.EntryResponse;
import com.neology.parking.dto.ExitResponse;
import com.neology.parking.dto.StayOverviewResponse;
import com.neology.parking.dto.StayRequest;
import java.util.List;

public interface ParkingService {
    EntryResponse registerEntry(StayRequest request);
    ExitResponse registerExit(StayRequest request);
    List<StayOverviewResponse> listStays();
}
