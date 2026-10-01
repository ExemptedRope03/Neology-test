package com.neology.parking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.neology.parking.config.ApiResponseAdvice;
import com.neology.parking.dto.VehicleResponse;
import com.neology.parking.dto.UpdateVehicleStatusRequest;
import com.neology.parking.entity.VehicleType;
import com.neology.parking.entity.VehicleStatus;
import com.neology.parking.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {
    @Mock private VehicleService vehicleService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new VehicleController(vehicleService))
                .setControllerAdvice(new ApiResponseAdvice())
                .build();
    }

    @Test
    void createsOfficialVehicleAndWrapsResponse() throws Exception {
        when(vehicleService.create(any(), eq(VehicleType.OFFICIAL)))
                .thenReturn(new VehicleResponse("OFI-001", VehicleType.OFFICIAL, VehicleStatus.ACTIVE));

        mockMvc.perform(post("/neo/vehiculos/oficiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placa\":\"OFI-001\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.succes").value(true))
                .andExpect(jsonPath("$.data.placa").value("OFI-001"))
                .andExpect(jsonPath("$.data.tipo").value("OFFICIAL"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void changesVehicleStatusAndWrapsResponse() throws Exception {
        when(vehicleService.changeStatus(eq("OFI-001"), any(UpdateVehicleStatusRequest.class)))
                .thenReturn(new VehicleResponse("OFI-001", VehicleType.OFFICIAL, VehicleStatus.INACTIVE));

        mockMvc.perform(patch("/neo/vehiculos/OFI-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.succes").value(true))
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }
}
