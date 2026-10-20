package com.myvehiclechoice;

import com.myvehiclechoice.dto.CompareSearchResultDto;
import com.myvehiclechoice.dto.VehicleDetailDto;
import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.model.entity.Brand;
import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.entity.VehicleVariant;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.VehicleImageRepository;
import com.myvehiclechoice.repository.VehicleRepository;
import com.myvehiclechoice.repository.VehicleVariantRepository;
import com.myvehiclechoice.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleVariantRepository variantRepository;

    @Mock
    private VehicleImageRepository imageRepository;

    private VehicleService vehicleService;

    private Vehicle testVehicle;
    private VehicleVariant testVariant;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService(vehicleRepository, variantRepository, imageRepository);

        testBrand = new Brand(1L, "Tata Motors", "India", "https://logo.png", true);

        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setBrand(testBrand);
        testVehicle.setModelName("Nexon");
        testVehicle.setCategory(VehicleCategory.CAR);
        testVehicle.setBodyStyle("SUV");
        testVehicle.setHeadline("5-Star B-NCAP Safety");
        testVehicle.setHeroImageUrl("https://nexon.jpg");
        testVehicle.setIsTrending(true);
        testVehicle.setIsFeatured(true);

        testVariant = new VehicleVariant();
        testVariant.setId(10L);
        testVariant.setVehicle(testVehicle);
        testVariant.setVariantName("Pure 1.2 Petrol 6MT");
        testVariant.setModelYear(2026);
        testVariant.setFuelType(FuelType.PETROL);
        testVariant.setTransmission(TransmissionType.MANUAL);
        testVariant.setExShowroomPrice(new BigDecimal("979000.00"));
        testVariant.setEstimatedOnRoadPrice(new BigDecimal("1115000.00"));
        testVariant.setMileageKmpl(new BigDecimal("17.44"));
        testVariant.setSafetyRatingStars(5);
        testVariant.setSafetyRatingProtocol("Bharat NCAP");
    }

    @Test
    @DisplayName("Should return trending vehicles with correctly mapped starting prices")
    void testGetTrendingVehicles() {
        when(vehicleRepository.findByIsTrendingTrue()).thenReturn(List.of(testVehicle));
        when(variantRepository.findByVehicleIdOrderByEstimatedOnRoadPriceAsc(1L)).thenReturn(List.of(testVariant));

        List<VehicleSummaryDto> trending = vehicleService.getTrendingVehicles();

        assertNotNull(trending);
        assertEquals(1, trending.size());
        VehicleSummaryDto summary = trending.get(0);
        assertEquals("Nexon", summary.getModelName());
        assertEquals("Tata Motors", summary.getBrandName());
        assertEquals(new BigDecimal("1115000.00"), summary.getStartingOnRoadPrice());
        assertEquals("17.44 kmpl Mileage", summary.getPrimaryKeySpec());
    }

    @Test
    @DisplayName("Should fetch complete vehicle details by ID including variants")
    void testGetVehicleById() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(variantRepository.findByVehicleIdOrderByEstimatedOnRoadPriceAsc(1L)).thenReturn(List.of(testVariant));
        when(imageRepository.findByVehicleIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());

        VehicleDetailDto detail = vehicleService.getVehicleById(1L);

        assertNotNull(detail);
        assertEquals("Nexon", detail.getModelName());
        assertEquals(1, detail.getVariants().size());
        assertEquals("Pure 1.2 Petrol 6MT", detail.getVariants().get(0).getVariantName());
        assertEquals(5, detail.getVariants().get(0).getSafetyRatingStars());
    }

    @Test
    @DisplayName("Should search variants for direct comparison autocomplete")
    void testSearchVariantsForCompare() {
        when(variantRepository.searchVariantsForCompare(eq("Nexon"), any(Pageable.class)))
                .thenReturn(List.of(testVariant));

        List<CompareSearchResultDto> results = vehicleService.searchVariantsForCompare("Nexon", 10);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Tata Motors Nexon Pure 1.2 Petrol 6MT", results.get(0).getFullName());
        assertEquals(new BigDecimal("1115000.00"), results.get(0).getEstimatedOnRoadPrice());
    }
}
