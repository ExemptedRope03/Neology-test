package com.neology.parking.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.neology.parking.dto.StayRequest;
import com.neology.parking.entity.Resident;
import com.neology.parking.entity.Stay;
import com.neology.parking.entity.Vehicle;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.repository.ResidentRepository;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ParkingServiceTest {

    @Test
    void exitOfResidentAccumulatesMinutesWithoutChargingAtExit() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-10T10:00:00Z"), ZoneOffset.UTC);
        Stay stay = new Stay("ABC123", LocalDateTime.of(2026, 1, 10, 9, 12));
        Resident resident = new Resident("ABC123");
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.RESIDENT);

        VehicleRepository vehicles = stub(VehicleRepository.class, (method, args) ->
                method.getName().equals("findById") ? Optional.of(vehicle) : unsupported(method.getName()));
        StayRepository stays = stub(StayRepository.class, (method, args) -> switch (method.getName()) {
            case "findByPlateAndExitAtIsNull" -> Optional.of(stay);
            case "save" -> args[0];
            default -> unsupported(method.getName());
        });
        ResidentRepository residents = stub(ResidentRepository.class, (method, args) -> switch (method.getName()) {
            case "findById" -> Optional.of(resident);
            case "save" -> args[0];
            default -> unsupported(method.getName());
        });
        PricingService prices = new PricingService(java.util.List.of(new ResidentPricingPolicy()));
        ParkingService service = new ParkingService(vehicles, stays, residents, prices, clock);

        var result = service.registerExit(new StayRequest("abc123"));

        assertThat(result.minutos()).isEqualTo(48);
        assertThat(result.importeACobrar()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resident.getAccumulatedMinutes()).isEqualTo(48);
        assertThat(stay.getExitAt()).isEqualTo(LocalDateTime.of(2026, 1, 10, 10, 0));
    }

    @SuppressWarnings("unchecked")
    private static <T> T stub(Class<T> type, RepositoryCall call) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
                (proxy, method, args) -> call.invoke(method, args));
    }

    private static Object unsupported(String method) {
        throw new UnsupportedOperationException("Método no usado por la prueba: " + method);
    }

    @FunctionalInterface
    private interface RepositoryCall {
        Object invoke(java.lang.reflect.Method method, Object[] args);
    }
}
