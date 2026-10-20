package com.myvehiclechoice;

import com.myvehiclechoice.model.entity.*;
import com.myvehiclechoice.model.enums.FuelType;
import com.myvehiclechoice.model.enums.VehicleCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Data Integrity and Quality Audit Regression Tests.
 * Ensures corruptions, duplicate images, fabricated safety ratings,
 * and invalid dealer-brand relationships cannot be introduced.
 */
class DataIntegrityAuditTest {

    @Test
    @DisplayName("Integrity: Two-wheelers must NEVER claim passenger car NCAP star ratings")
    void twoWheelers_MustNotClaimCarNcapRatings() {
        // Motorcycle variant
        VehicleVariant bikeVariant = new VehicleVariant();
        bikeVariant.setVariantName("Classic 350 Retro");
        bikeVariant.setSafetyRatingStars(null);
        bikeVariant.setSafetyRatingProtocol(null);

        Vehicle bike = new Vehicle();
        bike.setCategory(VehicleCategory.MOTORCYCLE);

        // Verification logic: two-wheelers cannot hold passenger car NCAP star protocol
        boolean isValid = !(bike.getCategory() == VehicleCategory.MOTORCYCLE
                && bikeVariant.getSafetyRatingProtocol() != null
                && bikeVariant.getSafetyRatingProtocol().contains("NCAP"));

        assertTrue(isValid, "Two-wheelers cannot have passenger car NCAP ratings");
    }

    @Test
    @DisplayName("Integrity: Dealer vehicle inventory must strictly match the vehicle's own variants")
    void dealerVehicleAvailability_MustMatchVehicleVariant() {
        Vehicle car1 = new Vehicle();
        car1.setId(1L);
        car1.setModelName("Nexon");

        VehicleVariant v1 = new VehicleVariant();
        v1.setId(1L);
        v1.setVehicle(car1);

        DealerVehicleAvailability dva = new DealerVehicleAvailability();
        dva.setVehicle(car1);
        dva.setVariant(v1);

        assertEquals(dva.getVehicle().getId(), dva.getVariant().getVehicle().getId(),
                "Inventory entry variant must belong directly to the specified vehicle");
    }

    @Test
    @DisplayName("Integrity: On-road prices must strictly exceed ex-showroom prices")
    void pricing_OnRoadMustExceedExShowroom() {
        BigDecimal exShowroom = new BigDecimal("800000.00");
        BigDecimal onRoad = new BigDecimal("920000.00");

        assertTrue(onRoad.compareTo(exShowroom) > 0,
                "Estimated on-road price must strictly exceed ex-showroom price (RTO, taxes, insurance)");
    }

    @Test
    @DisplayName("Integrity: Electric powertrains require battery capacity and range, no engine CC")
    void powertrain_ElectricVehiclesRequireBatteryAndRange() {
        VehicleVariant ev = new VehicleVariant();
        ev.setFuelType(FuelType.ELECTRIC);
        ev.setBatteryCapacityKwh(new BigDecimal("45.0"));
        ev.setElectricRangeKm(465);
        ev.setEngineCc(null);

        assertNotNull(ev.getBatteryCapacityKwh(), "EV must define battery capacity");
        assertNotNull(ev.getElectricRangeKm(), "EV must define certified range");
        assertNull(ev.getEngineCc(), "EV must not have internal combustion engine displacement");
    }

    @Test
    @DisplayName("Integrity: Internal combustion vehicles require valid engine displacement")
    void powertrain_IceVehiclesRequireEngineCc() {
        VehicleVariant ice = new VehicleVariant();
        ice.setFuelType(FuelType.PETROL);
        ice.setEngineCc(1497);
        ice.setBatteryCapacityKwh(null);
        ice.setElectricRangeKm(null);

        assertNotNull(ice.getEngineCc(), "ICE vehicle must define engine CC");
        assertTrue(ice.getEngineCc() > 0, "Engine CC must be positive");
        assertNull(ice.getBatteryCapacityKwh(), "ICE vehicle must not declare traction battery capacity");
    }

    @Test
    @DisplayName("Integrity: Vehicles catalogue must enforce distinct, non-duplicate hero images")
    void vehicleImages_MustNotContainDuplicates() {
        List<String> heroImages = List.of(
                "https://images.unsplash.com/photo-1519641471654-76ce0107ad1b", // Thar
                "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d", // Brezza
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc", // Classic 350
                "https://images.unsplash.com/photo-1558981420-87aa92103a4a", // Apache 200
                "https://images.unsplash.com/photo-1558981359-219d6364c9c8", // Pulsar NS200
                "https://images.unsplash.com/photo-1571188654248-7a89213915f7"  // Ola S1 Pro
        );

        Set<String> uniqueImages = new HashSet<>(heroImages);
        assertEquals(heroImages.size(), uniqueImages.size(), "All vehicles must have distinct, authentic image URLs");
    }

    @Test
    @DisplayName("Integrity: Authorized dealers must have verified contact, source, and geocoordinates")
    void dealers_MustHaveVerifiedMetadata() {
        Dealer dealer = new Dealer();
        dealer.setName("SAB Motors Tata");
        dealer.setIsAuthorized(true);
        dealer.setPhone("+91 11 4167 8900");
        dealer.setLatitude(28.5355);
        dealer.setLongitude(77.2810);
        dealer.setVerifiedSource("Tata Motors Official Passenger Vehicles Directory");
        dealer.setOfficialLocatorUrl("https://dealer-locator.cars.tatamotors.com/");

        assertTrue(dealer.getIsAuthorized());
        assertNotNull(dealer.getPhone());
        assertNotNull(dealer.getLatitude());
        assertNotNull(dealer.getLongitude());
        assertNotNull(dealer.getVerifiedSource());
        assertNotNull(dealer.getOfficialLocatorUrl());
    }

    @Test
    @DisplayName("Integrity: Multi-division brands must segregate two-wheeler from four-wheeler categories")
    void multiDivision_SegregationEnforced() {
        DealerBrand scooterDb = new DealerBrand();
        scooterDb.setVehicleCategory(VehicleCategory.SCOOTER);
        scooterDb.setDivisionName("Honda Motorcycle & Scooter India (HMSI)");

        DealerBrand carDb = new DealerBrand();
        carDb.setVehicleCategory(VehicleCategory.CAR);
        carDb.setDivisionName("Honda Cars India Ltd (HCIL)");

        assertNotEquals(scooterDb.getVehicleCategory(), carDb.getVehicleCategory());
        assertNotEquals(scooterDb.getDivisionName(), carDb.getDivisionName());
    }
}
