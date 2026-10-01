package com.neology.parking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neology.parking.dto.CreateVehicleRequest;
import com.neology.parking.dto.UpdateVehicleStatusRequest;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.entity.VehicleStatus;
import com.neology.parking.exception.BusinessRuleException;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import com.neology.parking.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {
    @Mock private VehicleRepository vehicleRepository;
    @Mock private ResidentRepository residentRepository;
    @Mock private StayRepository stayRepository;
    @Mock private PricingService pricingService;

    @Test
    void createsResidentAndInitializesItsAccumulatedTime() {
        VehicleService service = new VehicleServiceImpl(
                vehicleRepository, residentRepository, stayRepository, pricingService);
        when(vehicleRepository.existsById("ABC123")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.create(new CreateVehicleRequest("abc123"), VehicleType.RESIDENT);

        assertThat(result.placa()).isEqualTo("ABC123");
        assertThat(result.tipo()).isEqualTo(VehicleType.RESIDENT);
        assertThat(result.status()).isEqualTo(VehicleStatus.ACTIVE);
        ArgumentCaptor<Resident> residentCaptor = ArgumentCaptor.forClass(Resident.class);
        verify(residentRepository).save(residentCaptor.capture());
        assertThat(residentCaptor.getValue().getPlate()).isEqualTo("ABC123");
        assertThat(residentCaptor.getValue().getAccumulatedMinutes()).isZero();
    }

    @Test
    void rejectsDuplicatePlate() {
        VehicleService service = new VehicleServiceImpl(
                vehicleRepository, residentRepository, stayRepository, pricingService);
        when(vehicleRepository.existsById("ABC123")).thenReturn(true);

        assertThatThrownBy(() -> service.create(new CreateVehicleRequest("ABC123"), VehicleType.OFFICIAL))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Ya existe un vehículo");
    }

    @Test
    void changesVehicleStatus() {
        VehicleService service = new VehicleServiceImpl(
                vehicleRepository, residentRepository, stayRepository, pricingService);
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.OFFICIAL);
        when(vehicleRepository.findById("ABC123")).thenReturn(java.util.Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        var result = service.changeStatus("abc123", new UpdateVehicleStatusRequest(VehicleStatus.INACTIVE));

        assertThat(result.status()).isEqualTo(VehicleStatus.INACTIVE);
        assertThat(vehicle.getStatus()).isEqualTo(VehicleStatus.INACTIVE);
        verify(vehicleRepository).save(vehicle);
    }
}
