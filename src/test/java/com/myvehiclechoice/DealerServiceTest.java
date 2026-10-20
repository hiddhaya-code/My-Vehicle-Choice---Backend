package com.myvehiclechoice;

import com.myvehiclechoice.dto.DealerResponseDto;
import com.myvehiclechoice.model.entity.*;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.*;
import com.myvehiclechoice.service.DealerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {

    @Mock
    private DealerRepository dealerRepository;
    @Mock
    private DealerBrandRepository dealerBrandRepository;
    @Mock
    private DealerVehicleAvailabilityRepository availabilityRepository;
    @Mock
    private DealerEnquiryRepository enquiryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleVariantRepository variantRepository;
    @Mock
    private BrandRepository brandRepository;

    private DealerService dealerService;

    // Test Fixtures (All labeled as synthetic test data)
    private Brand tataBrand;
    private Brand hyundaiBrand;
    private Brand bmwBrand;
    private Brand ktmBrand;
    private Brand yamahaBrand;
    private Brand reBrand;
    private Brand atherBrand;
    private Brand hondaBrand;

    private Vehicle tataNexon;
    private Vehicle ktmDuke;
    private Vehicle yamahaR15;
    private Vehicle ather450X;
    private Vehicle hondaActiva;
    private Vehicle hondaCity;

    private Dealer tataSalesDealer;
    private Dealer tataServiceOnlyDealer;
    private Dealer hyundaiDealer;
    private Dealer bmwDealer;
    private Dealer ktmDealer;
    private Dealer yamahaDealer;
    private Dealer hondaTwoWheelerDealer;
    private Dealer hondaCarDealer;
    private Dealer atherDealer;

    @BeforeEach
    void setUp() {
        dealerService = new DealerService(
                dealerRepository,
                dealerBrandRepository,
                availabilityRepository,
                enquiryRepository,
                userRepository,
                vehicleRepository,
                variantRepository,
                brandRepository
        );

        // Brands
        tataBrand = new Brand(1L, "Tata Motors", "India", "https://example.com/tata.png", true);
        tataBrand.setOfficialDealerLocatorUrl("https://dealer-locator.cars.tatamotors.com/");

        hyundaiBrand = new Brand(2L, "Hyundai", "South Korea", "https://example.com/hyundai.png", true);
        hyundaiBrand.setOfficialDealerLocatorUrl("https://www.hyundai.com/in/en/click-to-buy/find-a-dealer-and-website");

        bmwBrand = new Brand(14L, "BMW", "Germany", "https://example.com/bmw.png", true);
        bmwBrand.setOfficialDealerLocatorUrl("https://www.bmw.in/en/fastlane/dealer-locator.html");

        ktmBrand = new Brand(4L, "KTM", "Austria", "https://example.com/ktm.png", true);
        ktmBrand.setOfficialDealerLocatorUrl("https://www.ktmindia.com/ktm-dealers");

        yamahaBrand = new Brand(5L, "Yamaha", "Japan", "https://example.com/yamaha.png", true);
        yamahaBrand.setOfficialDealerLocatorUrl("https://www.yamaha-motor-india.com/dealer-locator.html");

        reBrand = new Brand(6L, "Royal Enfield", "India", "https://example.com/re.png", true);
        reBrand.setOfficialDealerLocatorUrl("https://dealers.royalenfield.com/");

        atherBrand = new Brand(7L, "Ather Energy", "India", "https://example.com/ather.png", true);
        atherBrand.setOfficialDealerLocatorUrl("https://www.atherenergy.com/experience-centres");

        hondaBrand = new Brand(8L, "Honda", "Japan", "https://example.com/honda.png", true);
        hondaBrand.setOfficialDealerLocatorUrl("https://www.hondacarindia.com/dealer-locator");

        // Vehicles
        tataNexon = new Vehicle();
        tataNexon.setId(1L);
        tataNexon.setBrand(tataBrand);
        tataNexon.setModelName("Nexon");
        tataNexon.setCategory(VehicleCategory.CAR);

        ktmDuke = new Vehicle();
        ktmDuke.setId(5L);
        ktmDuke.setBrand(ktmBrand);
        ktmDuke.setModelName("200 Duke");
        ktmDuke.setCategory(VehicleCategory.MOTORCYCLE);

        yamahaR15 = new Vehicle();
        yamahaR15.setId(6L);
        yamahaR15.setBrand(yamahaBrand);
        yamahaR15.setModelName("YZF R15 V4");
        yamahaR15.setCategory(VehicleCategory.MOTORCYCLE);

        ather450X = new Vehicle();
        ather450X.setId(8L);
        ather450X.setBrand(atherBrand);
        ather450X.setModelName("450X Gen 3");
        ather450X.setCategory(VehicleCategory.ELECTRIC_TWO_WHEELER);

        hondaActiva = new Vehicle();
        hondaActiva.setId(9L);
        hondaActiva.setBrand(hondaBrand);
        hondaActiva.setModelName("Activa 6G");
        hondaActiva.setCategory(VehicleCategory.SCOOTER);

        hondaCity = new Vehicle();
        hondaCity.setId(14L);
        hondaCity.setBrand(hondaBrand);
        hondaCity.setModelName("City Gen 5");
        hondaCity.setCategory(VehicleCategory.CAR);

        // Dealers
        tataSalesDealer = new Dealer();
        tataSalesDealer.setId(1L);
        tataSalesDealer.setName("SAB Motors Tata Authorized Dealership [TEST]");
        tataSalesDealer.setBrandName("Tata Motors");
        tataSalesDealer.setDealerType("SALES_AND_SERVICE");
        tataSalesDealer.setIsAuthorized(true);
        tataSalesDealer.setCity("New Delhi");
        tataSalesDealer.setState("Delhi");
        tataSalesDealer.setPincode("110044");
        tataSalesDealer.setLatitude(28.5355);
        tataSalesDealer.setLongitude(77.2831);

        tataServiceOnlyDealer = new Dealer();
        tataServiceOnlyDealer.setId(16L);
        tataServiceOnlyDealer.setName("Tata Motors Okhla Fast-Track Workshop [TEST]");
        tataServiceOnlyDealer.setBrandName("Tata Motors");
        tataServiceOnlyDealer.setDealerType("SERVICE");
        tataServiceOnlyDealer.setIsAuthorized(true);
        tataServiceOnlyDealer.setCity("New Delhi");
        tataServiceOnlyDealer.setLatitude(28.5250);
        tataServiceOnlyDealer.setLongitude(77.2760);

        hyundaiDealer = new Dealer();
        hyundaiDealer.setId(2L);
        hyundaiDealer.setName("Koncept Hyundai Authorized Showroom [TEST]");
        hyundaiDealer.setBrandName("Hyundai");
        hyundaiDealer.setDealerType("SALES_AND_SERVICE");
        hyundaiDealer.setIsAuthorized(true);
        hyundaiDealer.setCity("New Delhi");

        bmwDealer = new Dealer();
        bmwDealer.setId(7L);
        bmwDealer.setName("Deutsche Motoren BMW Authorized Showroom [TEST]");
        bmwDealer.setBrandName("BMW");
        bmwDealer.setDealerType("SALES");
        bmwDealer.setIsAuthorized(true);
        bmwDealer.setCity("New Delhi");

        ktmDealer = new Dealer();
        ktmDealer.setId(4L);
        ktmDealer.setName("KTM South Extension [TEST]");
        ktmDealer.setBrandName("KTM");
        ktmDealer.setDealerType("SALES");
        ktmDealer.setIsAuthorized(true);
        ktmDealer.setCity("New Delhi");

        yamahaDealer = new Dealer();
        yamahaDealer.setId(10L);
        yamahaDealer.setName("Bangalore Yamaha City Motors [TEST]");
        yamahaDealer.setBrandName("Yamaha");
        yamahaDealer.setDealerType("SALES");
        yamahaDealer.setIsAuthorized(true);
        yamahaDealer.setCity("Bengaluru");
        yamahaDealer.setState("Karnataka");
        yamahaDealer.setLatitude(12.9600);
        yamahaDealer.setLongitude(77.6000);

        atherDealer = new Dealer();
        atherDealer.setId(5L);
        atherDealer.setName("Ather Space Indiranagar [TEST]");
        atherDealer.setBrandName("Ather Energy");
        atherDealer.setDealerType("SALES");
        atherDealer.setIsAuthorized(true);
        atherDealer.setCity("Bengaluru");
        atherDealer.setLatitude(12.9716);
        atherDealer.setLongitude(77.6412);

        hondaTwoWheelerDealer = new Dealer();
        hondaTwoWheelerDealer.setId(8L);
        hondaTwoWheelerDealer.setName("Prime Honda Two-Wheeler Showroom [TEST]");
        hondaTwoWheelerDealer.setBrandName("Honda");
        hondaTwoWheelerDealer.setDealerType("SALES_AND_SERVICE");
        hondaTwoWheelerDealer.setIsAuthorized(true);
        hondaTwoWheelerDealer.setCity("Mumbai");

        hondaCarDealer = new Dealer();
        hondaCarDealer.setId(9L);
        hondaCarDealer.setName("Apex Honda Cars Exclusive Showroom [TEST]");
        hondaCarDealer.setBrandName("Honda");
        hondaCarDealer.setDealerType("SALES_AND_SERVICE");
        hondaCarDealer.setIsAuthorized(true);
        hondaCarDealer.setCity("New Delhi");
    }

    @Test
    @DisplayName("Regression: Tata Nexon NEVER returns Hyundai or BMW dealership")
    void tataNexon_NeverReturnsHyundaiOrBmw() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        when(dealerRepository.findAuthorizedDealers(eq(1L), eq(VehicleCategory.CAR), eq("SALES"), eq("New Delhi"), any(), any()))
                .thenReturn(List.of(tataSalesDealer));

        DealerBrand db = new DealerBrand(tataSalesDealer, tataBrand, VehicleCategory.CAR, true, "Tata Motors Passenger Vehicles");
        when(dealerBrandRepository.findByDealerId(1L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", "New Delhi", null, null, null, null, null
        );

        assertFalse(result.isEmpty(), "Should return matching Tata dealer");
        assertEquals(1, result.size());
        assertEquals("SAB Motors Tata Authorized Dealership [TEST]", result.get(0).getName());
        assertEquals("Tata Motors", result.get(0).getBrandName());
        assertTrue(result.stream().noneMatch(d -> d.getName().contains("Hyundai") || d.getName().contains("BMW")));
    }

    @Test
    @DisplayName("Regression: KTM Duke 200 NEVER returns Yamaha or Royal Enfield dealership")
    void ktmDuke200_NeverReturnsYamahaOrRoyalEnfield() {
        when(vehicleRepository.findById(5L)).thenReturn(Optional.of(ktmDuke));
        when(dealerRepository.findAuthorizedDealers(eq(4L), eq(VehicleCategory.MOTORCYCLE), eq("SALES"), eq("New Delhi"), any(), any()))
                .thenReturn(List.of(ktmDealer));

        DealerBrand db = new DealerBrand(ktmDealer, ktmBrand, VehicleCategory.MOTORCYCLE, true, "KTM India");
        when(dealerBrandRepository.findByDealerId(4L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                5L, null, null, null, null, "SALES", "New Delhi", null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("KTM", result.get(0).getBrandName());
        assertTrue(result.stream().noneMatch(d -> d.getName().contains("Yamaha") || d.getName().contains("Royal Enfield")));
    }

    @Test
    @DisplayName("Regression: Yamaha R15 V4 returns ONLY eligible Yamaha sales dealers")
    void yamahaR15V4_ReturnsOnlyEligibleYamahaSalesDealers() {
        when(vehicleRepository.findById(6L)).thenReturn(Optional.of(yamahaR15));
        when(dealerRepository.findAuthorizedDealers(eq(5L), eq(VehicleCategory.MOTORCYCLE), eq("SALES"), eq("Bengaluru"), any(), any()))
                .thenReturn(List.of(yamahaDealer));

        DealerBrand db = new DealerBrand(yamahaDealer, yamahaBrand, VehicleCategory.MOTORCYCLE, true, "India Yamaha Motor");
        when(dealerBrandRepository.findByDealerId(10L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                6L, null, null, null, null, "SALES", "Bengaluru", null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Yamaha", result.get(0).getBrandName());
        assertEquals("MOTORCYCLE", result.get(0).getVehicleCategory());
        assertFalse(result.get(0).getName().contains("BMW"), "Yamaha must never return BMW dealer");
    }

    @Test
    @DisplayName("Regression: Ather 450X returns ONLY eligible Ather electric two-wheeler dealers")
    void ather450X_ReturnsOnlyEligibleAtherSalesDealers() {
        when(vehicleRepository.findById(8L)).thenReturn(Optional.of(ather450X));
        when(dealerRepository.findAuthorizedDealers(eq(7L), eq(VehicleCategory.ELECTRIC_TWO_WHEELER), eq("SALES"), eq("Bengaluru"), any(), any()))
                .thenReturn(List.of(atherDealer));

        DealerBrand db = new DealerBrand(atherDealer, atherBrand, VehicleCategory.ELECTRIC_TWO_WHEELER, true, "Ather Energy");
        when(dealerBrandRepository.findByDealerId(5L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                8L, null, null, null, null, "SALES", "Bengaluru", null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Ather Energy", result.get(0).getBrandName());
        assertEquals("ELECTRIC_TWO_WHEELER", result.get(0).getVehicleCategory());
    }

    @Test
    @DisplayName("Regression: Honda Activa 6G returns HMSI Two-Wheeler dealers, NEVER Honda car dealerships")
    void hondaActiva6G_UsesHondaTwoWheelerDealers_NotHondaCarDealerships() {
        when(vehicleRepository.findById(9L)).thenReturn(Optional.of(hondaActiva));
        // Expect query with SCOOTER category
        when(dealerRepository.findAuthorizedDealers(eq(8L), eq(VehicleCategory.SCOOTER), eq("SALES"), eq("Mumbai"), any(), any()))
                .thenReturn(List.of(hondaTwoWheelerDealer));

        DealerBrand db = new DealerBrand(hondaTwoWheelerDealer, hondaBrand, VehicleCategory.SCOOTER, true, "Honda Motorcycle & Scooter India (HMSI)");
        when(dealerBrandRepository.findByDealerId(8L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                9L, null, null, null, null, "SALES", "Mumbai", null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Prime Honda Two-Wheeler Showroom [TEST]", result.get(0).getName());
        assertEquals("Honda Motorcycle & Scooter India (HMSI)", result.get(0).getDivisionName());
        assertFalse(result.get(0).getName().contains("Cars"), "Scooter inquiry must NEVER return Honda Cars showroom");
    }

    @Test
    @DisplayName("Regression: Honda City returns Honda Cars India dealers, NEVER HMSI two-wheeler dealers")
    void hondaCity_UsesHondaCarDealerships_NotTwoWheelerDealers() {
        when(vehicleRepository.findById(14L)).thenReturn(Optional.of(hondaCity));
        // Expect query with CAR category
        when(dealerRepository.findAuthorizedDealers(eq(8L), eq(VehicleCategory.CAR), eq("SALES"), eq("New Delhi"), any(), any()))
                .thenReturn(List.of(hondaCarDealer));

        DealerBrand db = new DealerBrand(hondaCarDealer, hondaBrand, VehicleCategory.CAR, true, "Honda Cars India Ltd (HCIL)");
        when(dealerBrandRepository.findByDealerId(9L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                14L, null, null, null, null, "SALES", "New Delhi", null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("Apex Honda Cars Exclusive Showroom [TEST]", result.get(0).getName());
        assertEquals("Honda Cars India Ltd (HCIL)", result.get(0).getDivisionName());
        assertFalse(result.get(0).getName().contains("Two-Wheeler"), "Car inquiry must NEVER return Two-Wheeler showroom");
    }

    @Test
    @DisplayName("Regression: Service-only dealer NEVER appears as sales showroom")
    void serviceOnlyDealer_NeverAppearsAsSalesShowroom() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        // Repository filters out SERVICE dealers when dealerType = "SALES"
        when(dealerRepository.findAuthorizedDealersNoLocation(eq(1L), eq(VehicleCategory.CAR), eq("SALES")))
                .thenReturn(List.of(tataSalesDealer)); // Only sales dealer returned by repo query

        DealerBrand db = new DealerBrand(tataSalesDealer, tataBrand, VehicleCategory.CAR, true, "Tata Motors Passenger Vehicles");
        when(dealerBrandRepository.findByDealerId(1L)).thenReturn(List.of(db));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertNotEquals("SERVICE", result.get(0).getDealerType(), "Sales search must never return SERVICE-only dealer");
    }

    @Test
    @DisplayName("Regression: Dealer with unknown model availability is marked NOT_VERIFIED, never in stock")
    void dealerWithUnknownModelAvailability_NotMarkedInStock() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        when(dealerRepository.findAuthorizedDealersNoLocation(eq(1L), eq(VehicleCategory.CAR), eq("SALES")))
                .thenReturn(List.of(tataSalesDealer));

        // Availability repository returns empty (no inventory entry)
        when(availabilityRepository.findByDealerIdAndVehicleId(1L, 1L)).thenReturn(Optional.empty());

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals("NOT_VERIFIED", result.get(0).getAvailabilityStatus(), "Unknown inventory must be explicitly NOT_VERIFIED");
        assertFalse(result.get(0).getTestDriveAvailable());
        assertNull(result.get(0).getEstimatedDeliveryDays());
    }

    @Test
    @DisplayName("Regression: Honest empty state returned when no matching authorized dealer exists")
    void noMatchingDealer_ReturnsHonestEmptyState() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        when(dealerRepository.findAuthorizedDealers(eq(1L), eq(VehicleCategory.CAR), eq("SALES"), eq("NonExistentCity"), any(), any()))
                .thenReturn(Collections.emptyList());

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", "NonExistentCity", null, null, null, null, null
        );

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Must return empty list instead of substituting another brand");
    }

    @Test
    @DisplayName("Regression: Location radius filtering correctly excludes dealers outside radius")
    void locationRadiusFilter_ExcludesDealersOutsideConfiguredRadius() {
        // SAB Motors: 28.5355, 77.2831 (Delhi)
        // User coords: Bengaluru (12.9716, 77.5946) -> Distance ~1740 km
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        when(dealerRepository.findAuthorizedDealersNoLocation(eq(1L), eq(VehicleCategory.CAR), eq("SALES")))
                .thenReturn(List.of(tataSalesDealer));

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", null, null, null, 12.9716, 77.5946, 50.0 // 50 km radius limit
        );

        // SAB Motors is in Delhi (~1740km away), so 50km radius must exclude it
        assertTrue(result.isEmpty(), "Dealer ~1740km away must be excluded by 50km radius filter");
    }

    @Test
    @DisplayName("Regression: Coordinates calculate Haversine distance and sort nearest first")
    void haversineDistance_CalculatesAndSortsNearestFirst() {
        // Dealer A: New Delhi (28.5355, 77.2831)
        // Dealer B: Bengaluru (12.9600, 77.6000)
        // User at Connaught Place Delhi: (28.6320, 77.2190) -> Dealer A is ~12km, Dealer B is ~1700km
        Dealer dealerA = tataSalesDealer; // Delhi
        Dealer dealerB = new Dealer();
        dealerB.setId(20L);
        dealerB.setName("Tata Prerana Motors Bengaluru [TEST]");
        dealerB.setBrandName("Tata Motors");
        dealerB.setDealerType("SALES");
        dealerB.setIsAuthorized(true);
        dealerB.setLatitude(12.9600);
        dealerB.setLongitude(77.6000);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(tataNexon));
        when(dealerRepository.findAuthorizedDealersNoLocation(eq(1L), eq(VehicleCategory.CAR), eq("SALES")))
                .thenReturn(List.of(dealerB, dealerA)); // Unordered list

        List<DealerResponseDto> result = dealerService.getAccurateDealers(
                1L, null, null, null, null, "SALES", null, null, null, 28.6320, 77.2190, null
        );

        assertEquals(2, result.size());
        assertEquals("SAB Motors Tata Authorized Dealership [TEST]", result.get(0).getName(), "Delhi dealer should be sorted first");
        assertNotNull(result.get(0).getDistanceKm());
        assertTrue(result.get(0).getDistanceKm() < 20.0, "Delhi dealer distance should be under 20km");
        assertTrue(result.get(1).getDistanceKm() > 1500.0, "Bengaluru dealer distance should be over 1500km");
    }
}
