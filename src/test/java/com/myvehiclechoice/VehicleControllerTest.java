package com.myvehiclechoice;

import com.myvehiclechoice.controller.VehicleController;
import com.myvehiclechoice.dto.CompareSearchResultDto;
import com.myvehiclechoice.dto.VehicleDetailDto;
import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.security.CustomUserDetailsService;
import com.myvehiclechoice.security.JwtTokenProvider;
import com.myvehiclechoice.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("GET /api/v1/vehicles/trending should return 200 with trending vehicles")
    void testGetTrendingVehiclesEndpoint() throws Exception {
        VehicleSummaryDto summary = new VehicleSummaryDto();
        summary.setId(1L);
        summary.setBrandName("Tata Motors");
        summary.setModelName("Nexon");
        summary.setCategory(VehicleCategory.CAR);
        summary.setStartingOnRoadPrice(new BigDecimal("1115000.00"));

        when(vehicleService.getTrendingVehicles()).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/v1/vehicles/trending")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].modelName").value("Nexon"))
                .andExpect(jsonPath("$.data[0].brandName").value("Tata Motors"));
    }

    @Test
    @DisplayName("GET /api/v1/vehicles/{id} should return 200 with full vehicle detail")
    void testGetVehicleByIdEndpoint() throws Exception {
        VehicleDetailDto detail = new VehicleDetailDto();
        detail.setId(1L);
        detail.setBrandName("Tata Motors");
        detail.setModelName("Nexon");
        detail.setCategory(VehicleCategory.CAR);

        when(vehicleService.getVehicleById(1L)).thenReturn(detail);

        mockMvc.perform(get("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.modelName").value("Nexon"));
    }

    @Test
    @DisplayName("GET /api/v1/vehicles/compare-search should return matching variants for autocomplete")
    void testCompareSearchEndpoint() throws Exception {
        CompareSearchResultDto result = new CompareSearchResultDto(
                5L, 7L, "KTM", "200 Duke", "Dual-Channel ABS",
                VehicleCategory.MOTORCYCLE, FuelType.PETROL, TransmissionType.MANUAL,
                new BigDecimal("232000.00"), "https://ktm.jpg"
        );

        when(vehicleService.searchVariantsForCompare(eq("KTM"), any(Integer.class)))
                .thenReturn(List.of(result));

        mockMvc.perform(get("/api/v1/vehicles/compare-search?query=KTM")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].fullName").value("KTM 200 Duke Dual-Channel ABS"))
                .andExpect(jsonPath("$.data[0].category").value("MOTORCYCLE"));
    }
}
