-- ============================================================================
-- MyVehicleChoice Database Migration: V7 Data Quality Audit & Integrity Constraints
-- ============================================================================

-- 1. Create Data Audit Log Table for Reversibility and Traceability
CREATE TABLE IF NOT EXISTS data_audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id BIGINT NOT NULL,
    field_name VARCHAR(50) NOT NULL,
    old_value TEXT NULL,
    new_value TEXT NULL,
    action_type VARCHAR(50) NOT NULL,
    audit_reason VARCHAR(255) NOT NULL,
    source_reference VARCHAR(255) NOT NULL,
    audit_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_tbl_rec (table_name, record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Audit and Remove Synthetic Safety Ratings on Two-Wheelers
INSERT INTO data_audit_log (table_name, record_id, field_name, old_value, new_value, action_type, audit_reason, source_reference, audit_date) VALUES
('vehicle_variants', 18, 'safety_rating_stars', '4', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 18, 'safety_rating_protocol', 'Global NCAP Standard', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 19, 'safety_rating_stars', '4', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 19, 'safety_rating_protocol', 'Global NCAP Standard', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 20, 'safety_rating_stars', '4', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 20, 'safety_rating_protocol', 'Global NCAP Standard', NULL, 'REMOVAL_FABRICATED', 'Two-wheelers are not evaluated under passenger car NCAP star protocols', 'Global NCAP Technical Protocol (Cars Only)', '2026-03-01'),
('vehicle_variants', 21, 'safety_rating_stars', '4', NULL, 'REMOVAL_FABRICATED', 'Electric scooters do not have official passenger car NCAP star certifications', 'SIAM / ARAI Homologation Regulations', '2026-03-01'),
('vehicle_variants', 21, 'safety_rating_protocol', 'Standard EV Protocol', NULL, 'REMOVAL_FABRICATED', 'Electric scooters do not have official passenger car NCAP star certifications', 'SIAM / ARAI Homologation Regulations', '2026-03-01');

UPDATE vehicle_variants
SET safety_rating_stars = NULL, safety_rating_protocol = NULL
WHERE id IN (18, 19, 20, 21);

-- 3. Audit and Fix Cross-Vehicle Variant Corruption in dealer_vehicle_availability
INSERT INTO data_audit_log (table_name, record_id, field_name, old_value, new_value, action_type, audit_reason, source_reference, audit_date) VALUES
('dealer_vehicle_availability', 2, 'variant_id', '4', '6', 'CORRECTION', 'Cross-vehicle variant pointer: Nexon EV was linked to Creta Diesel variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 3, 'variant_id', '2', '3', 'CORRECTION', 'Cross-vehicle variant pointer: Creta was linked to Nexon Diesel variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 5, 'variant_id', '5', '7', 'CORRECTION', 'Cross-vehicle variant pointer: 200 Duke was linked to Swift variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 6, 'variant_id', '8', '10', 'CORRECTION', 'Cross-vehicle variant pointer: Ather 450X was linked to Yamaha R15 variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 7, 'variant_id', '7', '9', 'CORRECTION', 'Cross-vehicle variant pointer: Hunter 350 was linked to KTM Duke variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 9, 'variant_id', '9', '11', 'CORRECTION', 'Cross-vehicle variant pointer: Activa 6G was linked to Hunter 350 variant', 'V6 Seed Integrity Correction', '2026-03-01'),
('dealer_vehicle_availability', 11, 'variant_id', '6', '8', 'CORRECTION', 'Cross-vehicle variant pointer: Yamaha R15 was linked to Nexon EV variant', 'V6 Seed Integrity Correction', '2026-03-01');

UPDATE dealer_vehicle_availability SET variant_id = 6 WHERE id = 2;
UPDATE dealer_vehicle_availability SET variant_id = 3 WHERE id = 3;
UPDATE dealer_vehicle_availability SET variant_id = 7 WHERE id = 5;
UPDATE dealer_vehicle_availability SET variant_id = 10 WHERE id = 6;
UPDATE dealer_vehicle_availability SET variant_id = 9 WHERE id = 7;
UPDATE dealer_vehicle_availability SET variant_id = 11 WHERE id = 9;
UPDATE dealer_vehicle_availability SET variant_id = 8 WHERE id = 11;

-- 4. Audit and De-duplicate Recycled Hero Images Across Vehicles
INSERT INTO data_audit_log (table_name, record_id, field_name, old_value, new_value, action_type, audit_reason, source_reference, audit_date) VALUES
('vehicles', 11, 'hero_image_url', 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 11 (Thar) recycled Vehicle 10 (XUV700) hero image', 'Automotive Visual Audit', '2026-03-01'),
('vehicles', 13, 'hero_image_url', 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 13 (Brezza) recycled Vehicle 3 (Swift) hero image', 'Automotive Visual Audit', '2026-03-01'),
('vehicles', 15, 'hero_image_url', 'https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 15 (Classic 350) recycled Vehicle 7 (Hunter 350) hero image', 'Automotive Visual Audit', '2026-03-01'),
('vehicles', 16, 'hero_image_url', 'https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1558981420-87aa92103a4a?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 16 (Apache RTR 200) recycled Vehicle 5 (200 Duke) hero image', 'Automotive Visual Audit', '2026-03-01'),
('vehicles', 17, 'hero_image_url', 'https://images.unsplash.com/photo-1609630875171-b1321377ee65?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1558981359-219d6364c9c8?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 17 (Pulsar NS200) recycled Vehicle 6 (Yamaha R15) hero image', 'Automotive Visual Audit', '2026-03-01'),
('vehicles', 18, 'hero_image_url', 'https://images.unsplash.com/photo-1558980664-3a031cf67ea8?auto=format&fit=crop&w=1200&q=80', 'https://images.unsplash.com/photo-1571188654248-7a89213915f7?auto=format&fit=crop&w=1200&q=80', 'IMAGE_DE_DUPLICATION', 'Vehicle 18 (Ola S1 Pro) recycled Vehicle 8 (Ather 450X) hero image', 'Automotive Visual Audit', '2026-03-01');

UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1519641471654-76ce0107ad1b?auto=format&fit=crop&w=1200&q=80' WHERE id = 11;
UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=1200&q=80' WHERE id = 13;
UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=1200&q=80' WHERE id = 15;
UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1558981420-87aa92103a4a?auto=format&fit=crop&w=1200&q=80' WHERE id = 16;
UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1558981359-219d6364c9c8?auto=format&fit=crop&w=1200&q=80' WHERE id = 17;
UPDATE vehicles SET hero_image_url = 'https://images.unsplash.com/photo-1571188654248-7a89213915f7?auto=format&fit=crop&w=1200&q=80' WHERE id = 18;

-- 5. Audit and Replace Placeholder Photos with Authentic Brand Vector Logos
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Tata_logo.svg/320px-Tata_logo.svg.png' WHERE id = 1;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Hyundai_Motor_Company_logo.svg/320px-Hyundai_Motor_Company_logo.svg.png' WHERE id = 2;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Maruti_Suzuki_Logo.svg/320px-Maruti_Suzuki_Logo.svg.png' WHERE id = 3;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/KTM-Logo.svg/320px-KTM-Logo.svg.png' WHERE id = 4;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Yamaha_Motor_Logo_%28full%29.svg/320px-Yamaha_Motor_Logo_%28full%29.svg.png' WHERE id = 5;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/Royal_Enfield_logo.svg/320px-Royal_Enfield_logo.svg.png' WHERE id = 6;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Ather_Energy_Logo.svg/320px-Ather_Energy_Logo.svg.png' WHERE id = 7;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Honda_Logo.svg/320px-Honda_Logo.svg.png' WHERE id = 8;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Mahindra_Auto_new_logo.svg/320px-Mahindra_Auto_new_logo.svg.png' WHERE id = 9;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Kia_logo.svg/320px-Kia_logo.svg.png' WHERE id = 10;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/TVS_Motor_Company_Logo.svg/320px-TVS_Motor_Company_Logo.svg.png' WHERE id = 11;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Bajaj_Auto_Logo.svg/320px-Bajaj_Auto_Logo.svg.png' WHERE id = 12;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Ola_Cabs_logo.svg/320px-Ola_Cabs_logo.svg.png' WHERE id = 13;
UPDATE brands SET logo_url = 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/BMW.svg/320px-BMW.svg.png' WHERE id = 14;

-- 6. Add Schema-Level Data Integrity Constraints
ALTER TABLE vehicle_variants
ADD CONSTRAINT chk_on_road_gte_ex_showroom CHECK (estimated_on_road_price >= ex_showroom_price),
ADD CONSTRAINT chk_ev_battery CHECK (fuel_type != 'ELECTRIC' OR (battery_capacity_kwh IS NOT NULL AND electric_range_km IS NOT NULL)),
ADD CONSTRAINT chk_ice_engine CHECK (fuel_type = 'ELECTRIC' OR engine_cc IS NOT NULL);
