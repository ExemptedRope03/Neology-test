package com.neology.parking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neology.parking.dto.StayRequest;
import com.neology.parking.entity.Stay;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.entity.VehicleStatus;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import com.neology.parking.service.impl.ParkingServiceImpl;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingServiceImplTest {
    @Mock private VehicleRepository vehicleRepository;
    @Mock private StayRepository stayRepository;
    @Mock private ResidentRepository residentRepository;
    @Mock private PricingService pricingService;

    private ParkingService parkingService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-10T10:00:00Z"), ZoneOffset.UTC);
        parkingService = new ParkingServiceImpl(
                vehicleRepository, stayRepository, residentRepository, pricingService, clock);
    }

    @Test
    void registersExitAndChargesNonResident() {
        Stay stay = new Stay("ABC123", LocalDateTime.of(2026, 1, 10, 9, 30));
        when(vehicleRepository.findById("ABC123"))
                .thenReturn(Optional.of(new Vehicle("ABC123", VehicleType.NON_RESIDENT)));
        when(stayRepository.findByPlateAndExitAtIsNull("ABC123")).thenReturn(Optional.of(stay));
        when(pricingService.calculate(VehicleType.NON_RESIDENT, 30)).thenReturn(new BigDecimal("15.00"));

        var result = parkingService.registerExit(new StayRequest("abc123"));

        assertThat(result.minutos()).isEqualTo(30);
        assertThat(result.importeACobrar()).isEqualByComparingTo("15.00");
        assertThat(stay.getExitAt()).isEqualTo(LocalDateTime.of(2026, 1, 10, 10, 0));
        verify(stayRepository).save(stay);
        verify(pricingService).calculate(VehicleType.NON_RESIDENT, 30);
    }

    @Test
    void registersEntryForExistingVehicle() {
        when(vehicleRepository.findById("ABC123"))
                .thenReturn(Optional.of(new Vehicle("ABC123", VehicleType.OFFICIAL)));
        when(stayRepository.findByPlateAndExitAtIsNull("ABC123")).thenReturn(Optional.empty());
        when(stayRepository.save(any(Stay.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = parkingService.registerEntry(new StayRequest("abc123"));

        assertThat(result.placa()).isEqualTo("ABC123");
        assertThat(result.fechaHoraEntrada()).isEqualTo(LocalDateTime.of(2026, 1, 10, 10, 0));
        verify(stayRepository).save(any(Stay.class));
    }

    @Test
    void rejectsEntryForInactiveVehicle() {
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.OFFICIAL);
        vehicle.setStatus(VehicleStatus.INACTIVE);
        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));

        assertThatThrownBy(() -> parkingService.registerEntry(new StayRequest("abc123")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("inactivo");
    }
}
