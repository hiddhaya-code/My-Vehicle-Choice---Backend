package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.recommendation.OwnershipAffordabilityDto;
import com.myvehiclechoice.dto.recommendation.QuestionnaireAnswersDto;
import com.myvehiclechoice.dto.recommendation.RecommendationResponseDto;
import com.myvehiclechoice.dto.recommendation.RecommendationResultDto;
import com.myvehiclechoice.model.entity.RecommendationHistory;
import com.myvehiclechoice.model.entity.User;
import com.myvehiclechoice.model.entity.Vehicle;
import com.myvehiclechoice.model.entity.VehicleVariant;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import com.myvehiclechoice.repository.RecommendationHistoryRepository;
import com.myvehiclechoice.repository.UserRepository;
import com.myvehiclechoice.repository.VehicleRepository;
import com.myvehiclechoice.repository.VehicleVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecommendationEngineService {

    private final VehicleRepository vehicleRepository;
    private final VehicleVariantRepository variantRepository;
    private final VehicleService vehicleService;
    private final UserRepository userRepository;
    private final RecommendationHistoryRepository historyRepository;

    // Standard Automotive Benchmark Tariffs in India (Delhi NCR Reference)
    private static final double PETROL_PRICE_INR = 96.72;
    private static final double DIESEL_PRICE_INR = 89.62;
    private static final double EV_HOME_TARIFF_INR_PER_KWH = 6.50;
    private static final double LOAN_ANNUAL_INTEREST_RATE = 0.095; // 9.5% APR
    private static final int LOAN_TENURE_MONTHS = 60; // 5 years

    public RecommendationEngineService(VehicleRepository vehicleRepository,
                                         VehicleVariantRepository variantRepository,
                                         VehicleService vehicleService,
                                         UserRepository userRepository,
                                         RecommendationHistoryRepository historyRepository) {
        this.vehicleRepository = vehicleRepository;
        this.variantRepository = variantRepository;
        this.vehicleService = vehicleService;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    /**
     * Deterministic Multi-Category Vehicle Recommendation Algorithm.
     * Evaluates passenger cars, electric cars, motorcycles, and electric scooters
     * using category-specific metrics, verified data confidence, and 5-year TCO affordability.
     */
    public RecommendationResponseDto evaluateRecommendations(QuestionnaireAnswersDto answers) {
        RecommendationResponseDto response = new RecommendationResponseDto();
        List<VehicleVariant> allVariants = variantRepository.findAll();

        BigDecimal budget = resolveMaxOnRoadBudget(answers);
        boolean isStrict = Boolean.TRUE.equals(answers.getIsStrictBudget());

        List<ScoredVariant> exactMatches = new ArrayList<>();
        List<ScoredVariant> nearMatches = new ArrayList<>();
        List<String> rejectionReasons = new ArrayList<>();

        for (VehicleVariant variant : allVariants) {
            Vehicle vehicle = variant.getVehicle();

            // -------------------------------------------------------------
            // PHASE A: HARD-CONSTRAINT FILTERING
            // -------------------------------------------------------------

            // 1. Vehicle Category Compatibility
            if (answers.getCategory() != null) {
                if (!isCategoryCompatible(vehicle.getCategory(), answers.getCategory())) {
                    continue; // Skip silently (different primary category)
                }
            }

            // 2. Body Style Filter (if specified and not ANY)
            if (answers.getBodyStyle() != null && !answers.getBodyStyle().equalsIgnoreCase("ANY")) {
                if (vehicle.getBodyStyle() != null && !vehicle.getBodyStyle().equalsIgnoreCase(answers.getBodyStyle())) {
                    continue;
                }
            }

            // 3. Powertrain / Fuel Filter
            if (answers.getPreferredFuels() != null && !answers.getPreferredFuels().isEmpty()) {
                String vFuel = variant.getFuelType().name();
                boolean fuelMatches = answers.getPreferredFuels().stream()
                        .anyMatch(f -> f.equalsIgnoreCase(vFuel));
                if (!fuelMatches) {
                    continue;
                }
            }

            // 4. Non-Negotiable: Zero Emissions / Pure Electric
            if (answers.getNonNegotiables() != null && answers.getNonNegotiables().contains("ZERO_EMISSIONS")) {
                if (variant.getFuelType() != FuelType.ELECTRIC) {
                    continue;
                }
            }

            // 5. Mandatory Seating Capacity (Cars only)
            boolean isCar = vehicle.getCategory() == VehicleCategory.CAR || vehicle.getCategory() == VehicleCategory.ELECTRIC_CAR;
            if (isCar && answers.getMinSeatingCapacity() != null && variant.getSeatingCapacity() != null) {
                if (variant.getSeatingCapacity() < answers.getMinSeatingCapacity()) {
                    continue;
                }
            }

            // 6. Mandatory Transmission (if marked in non-negotiables)
            if (answers.getPreferredTransmission() != null && !answers.getPreferredTransmission().equalsIgnoreCase("ANY")) {
                boolean wantsAuto = answers.getPreferredTransmission().equalsIgnoreCase("AUTOMATIC");
                boolean isAuto = variant.getTransmission().name().equalsIgnoreCase("AUTOMATIC")
                        || variant.getFuelType() == FuelType.ELECTRIC; // EVs are single-speed automatic

                if (answers.getNonNegotiables() != null && (answers.getNonNegotiables().contains("AUTOMATIC") || answers.getNonNegotiables().contains("MANUAL"))) {
                    if (wantsAuto && !isAuto) continue;
                    if (!wantsAuto && isAuto) continue;
                }
            }

            // 7. Non-Negotiable: 5-Star Safety Rating (Cars only)
            if (answers.getNonNegotiables() != null && answers.getNonNegotiables().contains("SAFETY_5_STAR")) {
                if (isCar) {
                    if (variant.getSafetyRatingStars() == null || variant.getSafetyRatingStars() < 5) {
                        continue;
                    }
                }
            }

            // 8. Non-Negotiable: Dual-Channel ABS (Two-wheelers)
            if (answers.getNonNegotiables() != null && answers.getNonNegotiables().contains("DUAL_ABS")) {
                if (!isCar) {
                    if (!Boolean.TRUE.equals(variant.getHasAbs())) {
                        continue;
                    }
                }
            }

            // 9. Budget Constraint Evaluation
            BigDecimal onRoadPrice = variant.getEstimatedOnRoadPrice();
            boolean fitsBudget = onRoadPrice.compareTo(budget) <= 0;
            BigDecimal nearBudgetThreshold = budget.multiply(new BigDecimal("1.10")); // Max +10% for near match
            boolean fitsNearBudget = onRoadPrice.compareTo(nearBudgetThreshold) <= 0;

            if (!fitsBudget && isStrict) {
                // In strict mode, out-of-budget vehicles are strictly rejected
                continue;
            }

            // -------------------------------------------------------------
            // PHASE B: PREFERENCE SCORING & DATA CONFIDENCE
            // -------------------------------------------------------------
            ScoredVariant scored = calculateDeterministicScores(variant, answers, budget);

            if (fitsBudget) {
                exactMatches.add(scored);
            } else if (fitsNearBudget && Boolean.TRUE.equals(answers.getAllowNearMatches())) {
                scored.isNearMatch = true;
                nearMatches.add(scored);
            }
        }

        // Sort candidates by overallScore descending
        exactMatches.sort((a, b) -> Double.compare(b.overallScore, a.overallScore));
        nearMatches.sort((a, b) -> Double.compare(b.overallScore, a.overallScore));

        // Group by distinct vehicle model to give diverse recommendations (best variant per vehicle)
        Map<Long, ScoredVariant> bestPerVehicle = new LinkedHashMap<>();
        for (ScoredVariant sv : exactMatches) {
            Long vId = sv.variant.getVehicle().getId();
            if (!bestPerVehicle.containsKey(vId)) {
                bestPerVehicle.put(vId, sv);
            }
        }
        List<ScoredVariant> topExact = bestPerVehicle.values().stream().limit(5).collect(Collectors.toList());

        Map<Long, ScoredVariant> bestNearPerVehicle = new LinkedHashMap<>();
        for (ScoredVariant sv : nearMatches) {
            Long vId = sv.variant.getVehicle().getId();
            if (!bestPerVehicle.containsKey(vId) && !bestNearPerVehicle.containsKey(vId)) {
                bestNearPerVehicle.put(vId, sv);
            }
        }
        List<ScoredVariant> topNear = bestNearPerVehicle.values().stream().limit(3).collect(Collectors.toList());

        // Assign Badges & Formulate Explanations
        assignBadgesAndRankExplanations(topExact);

        // Convert to Response DTO
        int rank = 1;
        for (ScoredVariant sv : topExact) {
            response.getMatchedResults().add(toResultDto(sv, rank++, answers));
        }

        int nearRank = 1;
        for (ScoredVariant sv : topNear) {
            response.getNearMatchResults().add(toResultDto(sv, nearRank++, answers));
        }

        if (topExact.isEmpty()) {
            response.getUnmatchedConstraintNotes().add(
                    String.format("No %s models strictly met your on-road budget limit of ₹%s with all active constraints. " +
                                  "Try increasing your budget or switching from strict to flexible budget mode.",
                            answers.getCategory() != null ? answers.getCategory().name() : "vehicle",
                            budget.toPlainString())
            );
        }

        response.setUserSummary(String.format(
                "Personalized recommendations calculated for %s target budget of ₹%s (%s driving profile, %d km/day).",
                answers.getCategory() != null ? answers.getCategory().name() : "Any Vehicle",
                budget.toPlainString(),
                answers.getPerformancePriority(),
                answers.getDailyDistanceKm() != null ? answers.getDailyDistanceKm() : 35
        ));

        return response;
    }

    private BigDecimal resolveMaxOnRoadBudget(QuestionnaireAnswersDto answers) {
        if (answers.getBudgetAmount() == null) {
            return new BigDecimal("1500000"); // Default ₹15L if not provided
        }

        if ("EMI".equalsIgnoreCase(answers.getBudgetType())) {
            // User provided Monthly EMI Budget (e.g. ₹20,000/mo)
            // PMT inversion: EMI * 47.53 loan factor / 0.80 down-payment factor = EMI * 59.41
            return answers.getBudgetAmount().multiply(new BigDecimal("59.41")).setScale(0, RoundingMode.HALF_UP);
        }

        return answers.getBudgetAmount();
    }

    private ScoredVariant calculateDeterministicScores(VehicleVariant variant, QuestionnaireAnswersDto answers, BigDecimal budget) {
        ScoredVariant sv = new ScoredVariant();
        sv.variant = variant;
        Vehicle vehicle = variant.getVehicle();
        boolean isCar = vehicle.getCategory() == VehicleCategory.CAR || vehicle.getCategory() == VehicleCategory.ELECTRIC_CAR;
        boolean isEv = variant.getFuelType() == FuelType.ELECTRIC;

        // Dynamic Priority Weights based on user's top priorities
        double wSafety = 0.25;
        double wMileage = 0.25;
        double wMaint = 0.20;
        double wComfort = 0.15;
        double wPerf = 0.15;

        List<String> priorities = answers.getTopPriorities() != null ? answers.getTopPriorities() : Collections.emptyList();
        if (priorities.contains("SAFETY")) {
            wSafety = 0.35; wMaint = 0.15;
        } else if (priorities.contains("MILEAGE") || priorities.contains("EFFICIENCY")) {
            wMileage = 0.35; wPerf = 0.10;
        } else if (priorities.contains("PERFORMANCE")) {
            wPerf = 0.30; wMileage = 0.15;
        } else if (priorities.contains("MAINTENANCE") || priorities.contains("RELIABILITY")) {
            wMaint = 0.30; wPerf = 0.10;
        }

        // 1. Safety Score (Category-Specific)
        double safetyScore;
        if (isCar) {
            if (variant.getSafetyRatingStars() != null) {
                // 5 Stars = 90, 4 Stars = 75, 3 Stars = 60
                safetyScore = variant.getSafetyRatingStars() * 15.0 + 15.0;
            } else {
                safetyScore = 50.0;
                sv.missingDataWarnings.add("Crash test protocol ratings unavailable; score estimated from safety equipment.");
            }
            if (Boolean.TRUE.equals(variant.getHasAbs())) safetyScore = Math.min(100.0, safetyScore + 5.0);
            if (Boolean.TRUE.equals(variant.getHasEsp())) safetyScore = Math.min(100.0, safetyScore + 5.0);
            if (variant.getAirbagsCount() != null && variant.getAirbagsCount() >= 6) safetyScore = Math.min(100.0, safetyScore + 5.0);
        } else {
            // Two-wheelers: ABS and Braking Integrity
            if (Boolean.TRUE.equals(variant.getHasAbs())) {
                safetyScore = 90.0; // Dual-channel or single-channel ABS
            } else {
                safetyScore = 65.0; // Standard combi-braking system (CBS)
            }
        }
        sv.scoreSafety = safetyScore;

        // 2. Efficiency & Mileage Score (Category-Specific)
        double mileageScore;
        if (isEv) {
            // EVs evaluated on range and energy running cost
            double range = variant.getElectricRangeKm() != null ? variant.getElectricRangeKm() : 150.0;
            double benchmarkRange = isCar ? 465.0 : 180.0;
            mileageScore = Math.min(100.0, (range / benchmarkRange) * 85.0 + 15.0); // +15 bonus for low cost/km
        } else {
            double kmpl = variant.getMileageKmpl() != null ? variant.getMileageKmpl().doubleValue() : 15.0;
            double benchmarkKmpl = isCar ? 25.0 : 50.0; // 25 kmpl for cars (Swift benchmark), 50 kmpl for bikes/scooters
            mileageScore = Math.min(100.0, (kmpl / benchmarkKmpl) * 100.0);
        }
        sv.scoreMileage = mileageScore;

        // 3. Maintenance & Reliability Score
        double maintScore = 75.0;
        if (variant.getMaintenanceEstimateAnnual() != null) {
            double annualMaint = variant.getMaintenanceEstimateAnnual().doubleValue();
            if (annualMaint <= 3500) maintScore = 96.0;
            else if (annualMaint <= 6500) maintScore = 88.0;
            else if (annualMaint <= 12000) maintScore = 78.0;
            else if (annualMaint <= 20000) maintScore = 68.0;
            else maintScore = 55.0; // Premium luxury upkeep
        }
        sv.scoreMaint = maintScore;

        // 4. Comfort & Practicality Score (Category-Specific)
        double comfortScore = 70.0;
        if (isCar) {
            if (variant.getGroundClearanceMm() != null && variant.getGroundClearanceMm() >= 190) {
                comfortScore += 15.0;
            }
            if (variant.getBootSpaceLitres() != null && variant.getBootSpaceLitres() >= 350) {
                comfortScore += 15.0;
            }
        } else {
            // Two-wheelers: Ergonomic accessibility & weight
            if (variant.getSeatHeightMm() != null) {
                int sh = variant.getSeatHeightMm();
                if (sh >= 785 && sh <= 805) comfortScore += 15.0; // Ideal Indian accessible seat height
            } else {
                comfortScore += 10.0; // Neutral
            }
            if (variant.getKerbWeightKg() != null && variant.getKerbWeightKg() <= 165) {
                comfortScore += 15.0; // Nimble city weight
            }
        }
        sv.scoreComfort = Math.min(100.0, comfortScore);

        // 5. Performance Score (Category-Specific)
        double perfScore = 65.0;
        if (isEv) {
            // Instant electric torque responsiveness
            perfScore = 85.0;
        } else if (isCar) {
            if (variant.getMaxPowerBhp() != null) {
                double bhp = variant.getMaxPowerBhp().doubleValue();
                if (bhp >= 180) perfScore = 98.0;
                else if (bhp >= 130) perfScore = 88.0;
                else if (bhp >= 100) perfScore = 78.0;
                else perfScore = 65.0;
            }
        } else {
            // Two-wheelers
            if (variant.getMaxPowerBhp() != null) {
                double bhp = variant.getMaxPowerBhp().doubleValue();
                if (bhp >= 20.0) perfScore = 95.0;
                else if (bhp >= 15.0) perfScore = 85.0;
                else if (bhp >= 9.0) perfScore = 75.0;
                else perfScore = 65.0;
            }
        }
        sv.scorePerf = Math.min(100.0, perfScore);

        // Calculate Overall Normalized Score (0 to 100)
        sv.overallScore = Math.round(
                (wSafety * sv.scoreSafety +
                 wMileage * sv.scoreMileage +
                 wMaint * sv.scoreMaint +
                 wComfort * sv.scoreComfort +
                 wPerf * sv.scorePerf) * 10.0
        ) / 10.0;

        // Calculate Data Confidence Score (0 to 100%)
        sv.dataConfidenceScore = calculateDataConfidence(variant);
        if (sv.dataConfidenceScore >= 85.0) {
            sv.dataConfidenceLevel = "HIGH_VERIFIED";
        } else if (sv.dataConfidenceScore >= 70.0) {
            sv.dataConfidenceLevel = "MEDIUM_VERIFIED";
        } else {
            sv.dataConfidenceLevel = "CAUTION_MISSING_DATA";
        }

        // Generate 3 Personalized Reasons & 1-2 Realistic Trade-offs
        generateReasonsAndTradeOffs(sv, answers);

        return sv;
    }

    private double calculateDataConfidence(VehicleVariant v) {
        double confidence = 0.0;
        if (Boolean.TRUE.equals(v.getIsVerified())) confidence += 30.0;
        if (v.getDataSource() != null && !v.getDataSource().isEmpty()) confidence += 25.0;
        if (v.getVerifiedDate() != null) confidence += 15.0;
        if (v.getMaxPowerBhp() != null && v.getMaxTorqueNm() != null) confidence += 15.0;
        if (v.getEstimatedOnRoadPrice() != null && v.getExShowroomPrice() != null) confidence += 15.0;
        return Math.min(100.0, confidence);
    }

    private void generateReasonsAndTradeOffs(ScoredVariant sv, QuestionnaireAnswersDto answers) {
        VehicleVariant v = sv.variant;
        Vehicle vehicle = v.getVehicle();
        boolean isCar = vehicle.getCategory() == VehicleCategory.CAR || vehicle.getCategory() == VehicleCategory.ELECTRIC_CAR;
        boolean isEv = v.getFuelType() == FuelType.ELECTRIC;

        // Reason 1: Budget & Financial Fit
        sv.reasons.add(String.format("Fits comfortably within your on-road budget at ₹%,.0f.",
                v.getEstimatedOnRoadPrice().doubleValue()));

        // Reason 2: Safety or Efficiency Fit
        if (isCar && v.getSafetyRatingStars() != null && v.getSafetyRatingStars() >= 4) {
            sv.reasons.add(String.format("Verified %d-Star %s occupant safety rating with %d standard airbags.",
                    v.getSafetyRatingStars(), v.getSafetyRatingProtocol(), v.getAirbagsCount() != null ? v.getAirbagsCount() : 6));
        } else if (isEv) {
            sv.reasons.add(String.format("Exceptional low running cost (~₹1.10/km) with %d km certified range.",
                    v.getElectricRangeKm() != null ? v.getElectricRangeKm() : 150));
        } else if (v.getMileageKmpl() != null && v.getMileageKmpl().doubleValue() >= 17.0) {
            sv.reasons.add(String.format("High fuel efficiency: %s kmpl certified ARAI mileage for low monthly running cost.",
                    v.getMileageKmpl().toPlainString()));
        } else if (Boolean.TRUE.equals(v.getHasAbs())) {
            sv.reasons.add("Equipped with verified anti-lock braking (ABS) for superior wet and emergency stopping power.");
        } else {
            sv.reasons.add("Strong long-term reliability backed by a proven manufacturer warranty and service network.");
        }

        // Reason 3: Commute / Practicality Match
        if (isCar && v.getGroundClearanceMm() != null && v.getGroundClearanceMm() >= 190) {
            sv.reasons.add(String.format("Generous %d mm ground clearance well-suited for rough urban and broken highway roads.",
                    v.getGroundClearanceMm()));
        } else if (!isCar && v.getKerbWeightKg() != null && v.getKerbWeightKg() <= 165) {
            sv.reasons.add(String.format("Agile %d kg kerb weight offering effortless city traffic maneuverability.",
                    v.getKerbWeightKg()));
        } else if (v.getMaxPowerBhp() != null && v.getMaxPowerBhp().doubleValue() >= 100.0) {
            sv.reasons.add(String.format("Energetic %s bhp powertrain providing effortless highway overtaking and cruising.",
                    v.getMaxPowerBhp().toPlainString()));
        } else {
            sv.reasons.add("Low annual maintenance cost projection with predictable ownership expenditure.");
        }

        // Realistic Trade-Offs (Compromises)
        if (v.getKeyDisadvantages() != null && !v.getKeyDisadvantages().isEmpty()) {
            sv.tradeOffs.add(v.getKeyDisadvantages());
        } else if (isEv) {
            sv.tradeOffs.add("Highway range decreases by 15-20% at sustained 90+ km/h speeds; requires planning for inter-city fast chargers.");
        } else if (isCar && v.getGroundClearanceMm() != null && v.getGroundClearanceMm() <= 150) {
            sv.tradeOffs.add("Low ground clearance demands caution over tall unscientific speed breakers.");
        } else if (!isCar) {
            sv.tradeOffs.add("Two-wheeler format lacks weather protection during monsoon and has limited secured luggage storage.");
        } else {
            sv.tradeOffs.add("High market demand and custom alloy options may entail 2 to 4 weeks delivery scheduling.");
        }
    }

    private void assignBadgesAndRankExplanations(List<ScoredVariant> list) {
        if (list.isEmpty()) return;

        // Top overall
        list.get(0).badge = "Best Overall Match";
        list.get(0).relativePositionExplanation = "Highest composite score matching your budget, safety, and commute preferences.";

        // Best for Efficiency
        list.stream()
                .max(Comparator.comparingDouble(a -> a.scoreMileage))
                .ifPresent(best -> {
                    if (best.badge == null) {
                        best.badge = "Best for Mileage";
                        best.relativePositionExplanation = "Offers the lowest estimated monthly running cost and top fuel efficiency in your cohort.";
                    }
                });

        // Best for Safety
        list.stream()
                .max(Comparator.comparingDouble(a -> a.scoreSafety))
                .ifPresent(best -> {
                    if (best.badge == null) {
                        best.badge = "Top Safety Pick";
                        best.relativePositionExplanation = "Boasts the highest crash safety rating and active electronic stabilization equipment.";
                    }
                });

        // Best Budget Option
        list.stream()
                .min(Comparator.comparing(a -> a.variant.getEstimatedOnRoadPrice()))
                .ifPresent(best -> {
                    if (best.badge == null) {
                        best.badge = "Best Value Budget Option";
                        best.relativePositionExplanation = "Lowest purchase price and smallest down-payment requirement while fulfilling your core needs.";
                    }
                });

        // Default explanation for remaining
        for (int i = 0; i < list.size(); i++) {
            ScoredVariant sv = list.get(i);
            if (sv.relativePositionExplanation == null) {
                sv.relativePositionExplanation = String.format("Ranked #%d offering a balanced trade-off between price, equipment, and running costs.", i + 1);
            }
        }
    }

    private RecommendationResultDto toResultDto(ScoredVariant sv, int rank, QuestionnaireAnswersDto answers) {
        RecommendationResultDto dto = new RecommendationResultDto();
        VehicleVariant variant = sv.variant;
        Vehicle vehicle = variant.getVehicle();

        dto.setRank(rank);
        dto.setVehicle(vehicleService.toSummaryDto(vehicle));
        dto.setRecommendedVariant(vehicleService.toVariantDto(variant));
        dto.setOverallScore(sv.overallScore);
        dto.setDataConfidenceScore(sv.dataConfidenceScore);
        dto.setDataConfidenceLevel(sv.dataConfidenceLevel);
        dto.setMatchBadge(sv.badge != null ? sv.badge : "Recommended Match");
        dto.setReasons(sv.reasons);
        dto.setTradeOffs(sv.tradeOffs);
        dto.setMissingDataWarnings(sv.missingDataWarnings);
        dto.setNearMatch(sv.isNearMatch);
        dto.setRelativePositionExplanation(sv.relativePositionExplanation);

        // Score Breakdown
        Map<String, Double> breakdown = new HashMap<>();
        breakdown.put("Safety", sv.scoreSafety);
        breakdown.put("Mileage & Efficiency", sv.scoreMileage);
        breakdown.put("Low Maintenance", sv.scoreMaint);
        breakdown.put("Comfort & Practicality", sv.scoreComfort);
        breakdown.put("Performance", sv.scorePerf);
        dto.setScoreBreakdown(breakdown);

        // Category-Specific Technical Specs
        dto.setCategorySpecs(buildCategorySpecs(variant));

        // Actual Ownership Affordability Model
        dto.setAffordability(calculateOwnershipAffordability(variant, answers));

        return dto;
    }

    private Map<String, String> buildCategorySpecs(VehicleVariant v) {
        Map<String, String> specs = new LinkedHashMap<>();
        Vehicle vehicle = v.getVehicle();
        boolean isCar = vehicle.getCategory() == VehicleCategory.CAR || vehicle.getCategory() == VehicleCategory.ELECTRIC_CAR;
        boolean isEv = v.getFuelType() == FuelType.ELECTRIC;

        if (isCar) {
            if (v.getSafetyRatingStars() != null) {
                specs.put("Safety Rating", v.getSafetyRatingStars() + "★ (" + v.getSafetyRatingProtocol() + ")");
            } else {
                specs.put("Safety Rating", "Not Crash Tested");
            }
            if (v.getGroundClearanceMm() != null) {
                specs.put("Ground Clearance", v.getGroundClearanceMm() + " mm");
            }
            if (v.getBootSpaceLitres() != null) {
                specs.put("Boot Space", v.getBootSpaceLitres() + " L");
            }
            if (v.getSeatingCapacity() != null) {
                specs.put("Seating", v.getSeatingCapacity() + " Seats");
            }
        } else {
            // Two-wheelers
            if (v.getSeatHeightMm() != null) {
                specs.put("Seat Height", v.getSeatHeightMm() + " mm");
            }
            if (v.getKerbWeightKg() != null) {
                specs.put("Kerb Weight", v.getKerbWeightKg() + " kg");
            }
            if (Boolean.TRUE.equals(v.getHasAbs())) {
                specs.put("Braking", "Anti-Lock Braking (ABS)");
            } else {
                specs.put("Braking", "Combi-Braking System (CBS)");
            }
            if (v.getFuelTankCapacityLitres() != null) {
                specs.put("Fuel Tank", v.getFuelTankCapacityLitres() + " L");
            }
        }

        if (isEv) {
            if (v.getBatteryCapacityKwh() != null) {
                specs.put("Battery Pack", v.getBatteryCapacityKwh() + " kWh");
            }
            if (v.getElectricRangeKm() != null) {
                specs.put("Certified Range", v.getElectricRangeKm() + " km");
            }
            specs.put("Running Cost", "~₹1.10 per km");
        } else {
            if (v.getEngineCc() != null) {
                specs.put("Engine", v.getEngineCc() + " cc");
            }
            if (v.getMileageKmpl() != null) {
                specs.put("Efficiency", v.getMileageKmpl() + " kmpl (ARAI)");
            }
        }

        return specs;
    }

    private OwnershipAffordabilityDto calculateOwnershipAffordability(VehicleVariant v, QuestionnaireAnswersDto answers) {
        OwnershipAffordabilityDto dto = new OwnershipAffordabilityDto();
        BigDecimal onRoad = v.getEstimatedOnRoadPrice();
        BigDecimal exShowroom = v.getExShowroomPrice();

        dto.setExShowroomPrice(exShowroom);
        dto.setOnRoadPrice(onRoad);

        // 1. Loan & EMI (20% Down payment, 80% Loan @ 9.5% for 60 months)
        BigDecimal downPayment = onRoad.multiply(new BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal loanAmount = onRoad.subtract(downPayment).setScale(2, RoundingMode.HALF_UP);
        dto.setDownPayment(downPayment);
        dto.setLoanAmount(loanAmount);

        double p = loanAmount.doubleValue();
        double r = LOAN_ANNUAL_INTEREST_RATE / 12.0;
        int n = LOAN_TENURE_MONTHS;
        double emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
        BigDecimal monthlyEmi = BigDecimal.valueOf(Math.round(emi));
        dto.setMonthlyEmi(monthlyEmi);

        // 2. Monthly Energy/Fuel Cost based on user's commute
        int dailyKm = answers.getDailyDistanceKm() != null ? answers.getDailyDistanceKm() : 35;
        double monthlyKm = dailyKm * 30.5;
        double monthlyFuelCost;

        if (v.getFuelType() == FuelType.ELECTRIC) {
            double range = v.getElectricRangeKm() != null && v.getElectricRangeKm() > 0 ? v.getElectricRangeKm() : 150.0;
            double battery = v.getBatteryCapacityKwh() != null ? v.getBatteryCapacityKwh().doubleValue() : 3.7;
            monthlyFuelCost = (monthlyKm / range) * battery * EV_HOME_TARIFF_INR_PER_KWH;
        } else if (v.getFuelType() == FuelType.DIESEL) {
            double kmpl = v.getMileageKmpl() != null && v.getMileageKmpl().doubleValue() > 0 ? v.getMileageKmpl().doubleValue() : 18.0;
            monthlyFuelCost = (monthlyKm / kmpl) * DIESEL_PRICE_INR;
        } else {
            // Petrol
            double kmpl = v.getMileageKmpl() != null && v.getMileageKmpl().doubleValue() > 0 ? v.getMileageKmpl().doubleValue() : 17.0;
            monthlyFuelCost = (monthlyKm / kmpl) * PETROL_PRICE_INR;
        }
        BigDecimal monthlyEnergy = BigDecimal.valueOf(Math.round(monthlyFuelCost));
        dto.setMonthlyFuelOrEnergyCost(monthlyEnergy);

        // 3. Maintenance & Insurance
        BigDecimal annualMaint = v.getMaintenanceEstimateAnnual() != null
                ? v.getMaintenanceEstimateAnnual()
                : new BigDecimal("6000");
        dto.setAnnualMaintenanceEstimate(annualMaint);

        // Annual insurance roughly 3.0% of ex-showroom
        BigDecimal annualIns = exShowroom.multiply(new BigDecimal("0.030")).setScale(0, RoundingMode.HALF_UP);
        dto.setAnnualInsuranceEstimate(annualIns);

        // 4. 5-Year Total Cost of Ownership (TCO)
        // TCO = DownPayment + (60 * EMI) + (60 * MonthlyFuel) + (5 * AnnualMaint) + (5 * AnnualIns)
        BigDecimal totalEmis = monthlyEmi.multiply(BigDecimal.valueOf(60));
        BigDecimal totalFuel = monthlyEnergy.multiply(BigDecimal.valueOf(60));
        BigDecimal totalMaint = annualMaint.multiply(BigDecimal.valueOf(5));
        BigDecimal totalIns = annualIns.multiply(BigDecimal.valueOf(5));

        BigDecimal tco = downPayment.add(totalEmis).add(totalFuel).add(totalMaint).add(totalIns);
        dto.setFiveYearTotalCostOfOwnership(tco);

        dto.setCalculationAssumptions(String.format(
                "Assumptions: 20%% down payment, 60-month loan @ 9.5%% APR, %d km daily commute (%s), " +
                "local fuel tariffs (Petrol ₹%.2f/L, Diesel ₹%.2f/L, EV home power ₹%.2f/kWh).",
                dailyKm, answers.getCity() != null ? answers.getCity() : "New Delhi",
                PETROL_PRICE_INR, DIESEL_PRICE_INR, EV_HOME_TARIFF_INR_PER_KWH
        ));

        return dto;
    }

    private boolean isCategoryCompatible(VehicleCategory actual, VehicleCategory requested) {
        if (actual == requested) return true;
        if (requested == VehicleCategory.CAR && actual == VehicleCategory.ELECTRIC_CAR) return true;
        if (requested == VehicleCategory.MOTORCYCLE && actual == VehicleCategory.ELECTRIC_TWO_WHEELER) return true;
        if (requested == VehicleCategory.SCOOTER && actual == VehicleCategory.ELECTRIC_TWO_WHEELER) return true;
        return false;
    }

    @Transactional
    public void saveRecommendationHistory(String userEmail, QuestionnaireAnswersDto answers, RecommendationResponseDto response) {
        User user = null;
        if (userEmail != null) {
            user = userRepository.findByEmailIgnoreCase(userEmail).orElse(null);
        }

        String topIds = response.getMatchedResults().stream()
                .map(r -> String.valueOf(r.getVehicle().getId()))
                .collect(Collectors.joining(","));

        RecommendationHistory history = new RecommendationHistory(
                user,
                UUID.randomUUID().toString(),
                answers.getBudgetAmount() != null ? answers.getBudgetAmount() : BigDecimal.ZERO,
                answers.getBudgetType(),
                answers.getCategory() != null ? answers.getCategory().name() : "ANY",
                topIds,
                response.getUserSummary()
        );

        historyRepository.save(history);
    }

    private static class ScoredVariant {
        VehicleVariant variant;
        double overallScore;
        double dataConfidenceScore;
        String dataConfidenceLevel;
        double scoreSafety;
        double scoreMileage;
        double scoreMaint;
        double scoreComfort;
        double scorePerf;
        String badge;
        String relativePositionExplanation;
        List<String> reasons = new ArrayList<>();
        List<String> tradeOffs = new ArrayList<>();
        List<String> missingDataWarnings = new ArrayList<>();
        boolean isNearMatch = false;
    }
}
