package com.myvehiclechoice;

import com.myvehiclechoice.dto.VehicleSummaryDto;
import com.myvehiclechoice.dto.VehicleVariantDto;
import com.myvehiclechoice.dto.recommendation.QuestionnaireAnswersDto;
import com.myvehiclechoice.dto.recommendation.RecommendationResponseDto;
import com.myvehiclechoice.dto.recommendation.RecommendationResultDto;
import com.myvehiclechoice.model.entity.Brand;
import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.entity.VehicleVariant;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.TransmissionType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.RecommendationHistoryRepository;
import com.myvehiclechoice.repository.UserRepository;
import com.myvehiclechoice.repository.VehicleRepository;
import com.myvehiclechoice.repository.VehicleVariantRepository;
import com.myvehiclechoice.service.RecommendationEngineService;
import com.myvehiclechoice.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationEngineServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleVariantRepository variantRepository;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationHistoryRepository historyRepository;

    private RecommendationEngineService recommendationEngine;

    private Vehicle carNexon;
    private VehicleVariant variantNexon;
    private Vehicle evNexon;
    private VehicleVariant variantNexonEv;
    private Vehicle bikeKtm;
    private VehicleVariant variantKtm;
    private Vehicle scooterActiva;
    private VehicleVariant variantActiva;

    @BeforeEach
    void setUp() {
        recommendationEngine = new RecommendationEngineService(
                vehicleRepository, variantRepository, vehicleService, userRepository, historyRepository
        );

        Brand tata = new Brand(1L, "Tata Motors", "India", "https://logo.png", true);

        // 1. Petrol Car: Tata Nexon
        carNexon = new Vehicle();
        carNexon.setId(1L);
        carNexon.setBrand(tata);
        carNexon.setModelName("Nexon");
        carNexon.setCategory(VehicleCategory.CAR);

        variantNexon = new VehicleVariant();
        variantNexon.setId(1L);
        variantNexon.setVehicle(carNexon);
        variantNexon.setVariantName("Pure Petrol 6MT");
        variantNexon.setFuelType(FuelType.PETROL);
        variantNexon.setTransmission(TransmissionType.MANUAL);
        variantNexon.setExShowroomPrice(new BigDecimal("979000.00"));
        variantNexon.setEstimatedOnRoadPrice(new BigDecimal("1115000.00")); // ₹11.15L
        variantNexon.setMileageKmpl(new BigDecimal("17.44"));
        variantNexon.setSafetyRatingStars(5);
        variantNexon.setSafetyRatingProtocol("Bharat NCAP");
        variantNexon.setAirbagsCount(6);
        variantNexon.setHasAbs(true);
        variantNexon.setHasEsp(true);
        variantNexon.setSeatingCapacity(5);
        variantNexon.setGroundClearanceMm(208);
        variantNexon.setBootSpaceLitres(382);
        variantNexon.setMaintenanceEstimateAnnual(new BigDecimal("6500.00"));
        variantNexon.setIsVerified(true);
        variantNexon.setDataSource("Tata Motors Official Brochure 2026");
        variantNexon.setVerifiedDate(LocalDate.of(2026, 2, 15));

        // 2. Electric Car: Tata Nexon EV
        evNexon = new Vehicle();
        evNexon.setId(4L);
        evNexon.setBrand(tata);
        evNexon.setModelName("Nexon EV");
        evNexon.setCategory(VehicleCategory.ELECTRIC_CAR);

        variantNexonEv = new VehicleVariant();
        variantNexonEv.setId(6L);
        variantNexonEv.setVehicle(evNexon);
        variantNexonEv.setVariantName("Empowered Plus 45");
        variantNexonEv.setFuelType(FuelType.ELECTRIC);
        variantNexonEv.setTransmission(TransmissionType.AUTOMATIC);
        variantNexonEv.setExShowroomPrice(new BigDecimal("1699000.00"));
        variantNexonEv.setEstimatedOnRoadPrice(new BigDecimal("1785000.00")); // ₹17.85L
        variantNexonEv.setElectricRangeKm(465);
        variantNexonEv.setBatteryCapacityKwh(new BigDecimal("45.00"));
        variantNexonEv.setSafetyRatingStars(5);
        variantNexonEv.setSafetyRatingProtocol("Bharat NCAP");
        variantNexonEv.setAirbagsCount(6);
        variantNexonEv.setHasAbs(true);
        variantNexonEv.setHasEsp(true);
        variantNexonEv.setSeatingCapacity(5);
        variantNexonEv.setMaintenanceEstimateAnnual(new BigDecimal("3500.00"));
        variantNexonEv.setIsVerified(true);
        variantNexonEv.setDataSource("Tata Passenger Electric Mobility 2026");
        variantNexonEv.setVerifiedDate(LocalDate.of(2026, 2, 28));

        // 3. Motorcycle: KTM 200 Duke
        Brand ktm = new Brand(4L, "KTM", "Austria", "https://ktm.png", true);
        bikeKtm = new Vehicle();
        bikeKtm.setId(5L);
        bikeKtm.setBrand(ktm);
        bikeKtm.setModelName("200 Duke");
        bikeKtm.setCategory(VehicleCategory.MOTORCYCLE);

        variantKtm = new VehicleVariant();
        variantKtm.setId(7L);
        variantKtm.setVehicle(bikeKtm);
        variantKtm.setVariantName("200 Duke Dual-Channel ABS");
        variantKtm.setFuelType(FuelType.PETROL);
        variantKtm.setTransmission(TransmissionType.MANUAL);
        variantKtm.setExShowroomPrice(new BigDecimal("199000.00"));
        variantKtm.setEstimatedOnRoadPrice(new BigDecimal("232000.00")); // ₹2.32L
        variantKtm.setMileageKmpl(new BigDecimal("33.00"));
        variantKtm.setMaxPowerBhp(new BigDecimal("24.60"));
        variantKtm.setHasAbs(true);
        variantKtm.setKerbWeightKg(159);
        variantKtm.setSeatHeightMm(822);
        variantKtm.setMaintenanceEstimateAnnual(new BigDecimal("4000.00"));
        variantKtm.setIsVerified(true);
        variantKtm.setDataSource("Bajaj Auto / KTM Official Spec 2026");
        variantKtm.setVerifiedDate(LocalDate.of(2026, 1, 20));

        // 4. Scooter: Honda Activa 6G
        Brand honda = new Brand(8L, "Honda", "Japan", "https://honda.png", true);
        scooterActiva = new Vehicle();
        scooterActiva.setId(9L);
        scooterActiva.setBrand(honda);
        scooterActiva.setModelName("Activa 6G");
        scooterActiva.setCategory(VehicleCategory.SCOOTER);

        variantActiva = new VehicleVariant();
        variantActiva.setId(11L);
        variantActiva.setVehicle(scooterActiva);
        variantActiva.setVariantName("Activa 6G Deluxe");
        variantActiva.setFuelType(FuelType.PETROL);
        variantActiva.setTransmission(TransmissionType.AUTOMATIC);
        variantActiva.setExShowroomPrice(new BigDecimal("79000.00"));
        variantActiva.setEstimatedOnRoadPrice(new BigDecimal("92000.00")); // ₹92k
        variantActiva.setMileageKmpl(new BigDecimal("50.00"));
        variantActiva.setMaxPowerBhp(new BigDecimal("7.70"));
        variantActiva.setKerbWeightKg(106);
        variantActiva.setSeatHeightMm(765);
        variantActiva.setMaintenanceEstimateAnnual(new BigDecimal("2200.00"));
        variantActiva.setIsVerified(true);
        variantActiva.setDataSource("Honda Motorcycle & Scooter India 2026");
        variantActiva.setVerifiedDate(LocalDate.of(2026, 1, 10));
    }

    private void mockDtoConversions() {
        when(vehicleService.toSummaryDto(any())).thenAnswer(invocation -> {
            Vehicle v = invocation.getArgument(0);
            VehicleSummaryDto s = new VehicleSummaryDto();
            s.setId(v.getId());
            s.setModelName(v.getModelName());
            s.setCategory(v.getCategory());
            return s;
        });

        when(vehicleService.toVariantDto(any())).thenAnswer(invocation -> {
            VehicleVariant var = invocation.getArgument(0);
            VehicleVariantDto dto = new VehicleVariantDto();
            dto.setId(var.getId());
            dto.setVariantName(var.getVariantName());
            dto.setExShowroomPrice(var.getExShowroomPrice());
            dto.setEstimatedOnRoadPrice(var.getEstimatedOnRoadPrice());
            return dto;
        });
    }

    @Test
    @DisplayName("Hard Constraint: Strict on-road budget strictly excludes out-of-budget models")
    void testStrictBudgetConstraint() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon, variantKtm, variantActiva));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("500000.00")); // ₹5 Lakh on-road budget
        answers.setIsStrictBudget(true);

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        assertNotNull(response);
        // Nexon (₹11.15L) must be strictly excluded
        assertTrue(response.getMatchedResults().stream().noneMatch(r -> r.getVehicle().getModelName().equals("Nexon")));
        // KTM (₹2.32L) and Activa (₹92k) fit budget
        assertEquals(2, response.getMatchedResults().size());
    }

    @Test
    @DisplayName("Budget Type: Monthly EMI budget correctly calculates maximum loan & on-road affordability")
    void testEmiBudgetTypeCalculation() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon, variantNexonEv));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetType("EMI");
        answers.setBudgetAmount(new BigDecimal("20000.00")); // ₹20,000/month EMI budget (~₹11.88L On-road cap)

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        // Nexon (₹11.15L on-road) fits ₹20k/mo EMI
        assertTrue(response.getMatchedResults().stream().anyMatch(r -> r.getVehicle().getModelName().equals("Nexon")));
        // Nexon EV (₹17.85L on-road) exceeds ₹20k/mo EMI cap
        assertTrue(response.getMatchedResults().stream().noneMatch(r -> r.getVehicle().getModelName().equals("Nexon EV")));
    }

    @Test
    @DisplayName("Category Isolation: Motorcycle inquiry never returns Cars, SUVs, or Scooters")
    void testMotorcycleCategoryIsolation() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon, variantKtm, variantActiva));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("1500000.00"));
        answers.setCategory(VehicleCategory.MOTORCYCLE);

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        assertEquals(1, response.getMatchedResults().size());
        assertEquals("200 Duke", response.getMatchedResults().get(0).getVehicle().getModelName());
    }

    @Test
    @DisplayName("Powertrain Exclusion: Pure Electric non-negotiable filters out ICE vehicles")
    void testZeroEmissionsFilter() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon, variantNexonEv));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("2000000.00")); // ₹20L
        answers.setCategory(VehicleCategory.CAR);
        answers.getNonNegotiables().add("ZERO_EMISSIONS");

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        assertEquals(1, response.getMatchedResults().size());
        assertEquals("Nexon EV", response.getMatchedResults().get(0).getVehicle().getModelName());
    }

    @Test
    @DisplayName("Explainability: Recommendation result includes 3 reasons, trade-offs, and high data confidence")
    void testExplainabilityAndDataConfidence() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("1500000.00"));
        answers.getTopPriorities().add("SAFETY");

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        assertFalse(response.getMatchedResults().isEmpty());
        RecommendationResultDto top = response.getMatchedResults().get(0);

        // 3 personalized reasons
        assertEquals(3, top.getReasons().size());
        // At least 1 honest trade-off
        assertFalse(top.getTradeOffs().isEmpty());
        // Data confidence score >= 85% with HIGH_VERIFIED badge
        assertTrue(top.getDataConfidenceScore() >= 85.0);
        assertEquals("HIGH_VERIFIED", top.getDataConfidenceLevel());
        // Category specs defined
        assertNotNull(top.getCategorySpecs());
        assertTrue(top.getCategorySpecs().containsKey("Safety Rating"));
    }

    @Test
    @DisplayName("Affordability: Calculates accurate 5-year TCO, monthly EMI, and monthly energy costs")
    void testOwnershipAffordabilityModeling() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon));
        mockDtoConversions();

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("1500000.00"));
        answers.setDailyDistanceKm(40); // 40 km/day

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        RecommendationResultDto top = response.getMatchedResults().get(0);
        assertNotNull(top.getAffordability());

        // On-road price ₹11.15L
        assertEquals(new BigDecimal("1115000.00"), top.getAffordability().getOnRoadPrice());
        // Down payment 20% = ₹2.23L
        assertEquals(new BigDecimal("223000.00"), top.getAffordability().getDownPayment());
        // Monthly EMI > ₹15,000
        assertTrue(top.getAffordability().getMonthlyEmi().doubleValue() > 15000.0);
        // Monthly fuel cost for 40 km/day @ 17.44 kmpl & ₹96.72/L petrol
        assertTrue(top.getAffordability().getMonthlyFuelOrEnergyCost().doubleValue() > 5000.0);
        // 5-Year TCO is positive and includes all parameters
        assertTrue(top.getAffordability().getFiveYearTotalCostOfOwnership().doubleValue() > 1200000.0);
    }

    @Test
    @DisplayName("Honest Empty State: Unmatched constraints produce clear advice without hallucinating vehicles")
    void testHonestEmptyState() {
        when(variantRepository.findAll()).thenReturn(List.of(variantNexon));

        QuestionnaireAnswersDto answers = new QuestionnaireAnswersDto();
        answers.setBudgetAmount(new BigDecimal("300000.00")); // ₹3L on-road for a car (impossible)
        answers.setCategory(VehicleCategory.CAR);
        answers.setIsStrictBudget(true);

        RecommendationResponseDto response = recommendationEngine.evaluateRecommendations(answers);

        assertTrue(response.getMatchedResults().isEmpty());
        assertFalse(response.getUnmatchedConstraintNotes().isEmpty());
        assertTrue(response.getUnmatchedConstraintNotes().get(0).contains("budget limit of ₹300000"));
    }
}
