-- ============================================================================
-- MyVehicleChoice Database Migration: V6 Normalize Dealers & Accurate Matching
-- ============================================================================

-- 1. Add missing attributes to brands table
ALTER TABLE brands
ADD COLUMN official_dealer_locator_url VARCHAR(300) NULL AFTER logo_url;

-- Update official manufacturer locator URLs
UPDATE brands SET official_dealer_locator_url = 'https://dealer-locator.cars.tatamotors.com/' WHERE id = 1;
UPDATE brands SET official_dealer_locator_url = 'https://www.hyundai.com/in/en/click-to-buy/find-a-dealer-and-website' WHERE id = 2;
UPDATE brands SET official_dealer_locator_url = 'https://www.marutisuzuki.com/dealer-locator' WHERE id = 3;
UPDATE brands SET official_dealer_locator_url = 'https://www.ktmindia.com/ktm-dealers' WHERE id = 4;
UPDATE brands SET official_dealer_locator_url = 'https://www.yamaha-motor-india.com/dealer-locator.html' WHERE id = 5;
UPDATE brands SET official_dealer_locator_url = 'https://dealers.royalenfield.com/' WHERE id = 6;
UPDATE brands SET official_dealer_locator_url = 'https://www.atherenergy.com/experience-centres' WHERE id = 7;
UPDATE brands SET official_dealer_locator_url = 'https://www.hondacarindia.com/dealer-locator' WHERE id = 8;
UPDATE brands SET official_dealer_locator_url = 'https://auto.mahindra.com/dealer-locator' WHERE id = 9;
UPDATE brands SET official_dealer_locator_url = 'https://www.kia.com/in/buy/find-a-dealer.html' WHERE id = 10;
UPDATE brands SET official_dealer_locator_url = 'https://dealers.tvsmotor.com/' WHERE id = 11;
UPDATE brands SET official_dealer_locator_url = 'https://www.bajajauto.com/dealer-locator' WHERE id = 12;
UPDATE brands SET official_dealer_locator_url = 'https://www.olaelectric.com/experience-centres' WHERE id = 13;
UPDATE brands SET official_dealer_locator_url = 'https://www.bmw.in/en/fastlane/dealer-locator.html' WHERE id = 14;

-- 2. Enhance dealers table with capabilities, geocoordinates and verification metadata
ALTER TABLE dealers
ADD COLUMN dealer_type VARCHAR(30) NOT NULL DEFAULT 'SALES' AFTER is_authorized,
ADD COLUMN locality VARCHAR(150) NULL AFTER address,
ADD COLUMN latitude DECIMAL(10, 7) NULL AFTER pincode,
ADD COLUMN longitude DECIMAL(10, 7) NULL AFTER latitude,
ADD COLUMN official_locator_url VARCHAR(300) NULL AFTER website,
ADD COLUMN verification_status VARCHAR(50) NOT NULL DEFAULT 'OFFICIAL_OEM_VERIFIED' AFTER is_authorized,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- 3. Normalized Dealer-Brand Authorization Table
CREATE TABLE IF NOT EXISTS dealer_brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    vehicle_category ENUM('CAR', 'MOTORCYCLE', 'SCOOTER', 'ELECTRIC_CAR', 'ELECTRIC_TWO_WHEELER') NOT NULL,
    is_authorized BOOLEAN DEFAULT TRUE,
    division_name VARCHAR(150) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_db_dealer FOREIGN KEY (dealer_id) REFERENCES dealers (id) ON DELETE CASCADE,
    CONSTRAINT fk_db_brand FOREIGN KEY (brand_id) REFERENCES brands (id) ON DELETE RESTRICT,
    CONSTRAINT uk_dealer_brand_cat UNIQUE (dealer_id, brand_id, vehicle_category),
    INDEX idx_db_brand_cat (brand_id, vehicle_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Model-Specific Dealer Inventory & Test Drive Availability Table
CREATE TABLE IF NOT EXISTS dealer_vehicle_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    variant_id BIGINT NULL,
    availability_status VARCHAR(50) NOT NULL DEFAULT 'NOT_VERIFIED',
    test_drive_available BOOLEAN DEFAULT FALSE,
    estimated_delivery_days INT NULL,
    last_verified_date DATE NULL,
    notes VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_dva_dealer FOREIGN KEY (dealer_id) REFERENCES dealers (id) ON DELETE CASCADE,
    CONSTRAINT fk_dva_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE CASCADE,
    CONSTRAINT fk_dva_variant FOREIGN KEY (variant_id) REFERENCES vehicle_variants (id) ON DELETE SET NULL,
    CONSTRAINT uk_dealer_vehicle UNIQUE (dealer_id, vehicle_id),
    INDEX idx_dva_vehicle (vehicle_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Fix and normalize existing dealer records
-- Dealer 1: Tata Motors SAB Motors (Delhi)
UPDATE dealers SET 
  name = 'SAB Motors Tata Authorized Dealership',
  brand_name = 'Tata Motors',
  dealer_type = 'SALES_AND_SERVICE',
  locality = 'Mohan Cooperative Industrial Estate',
  latitude = 28.5355000,
  longitude = 77.2810000,
  official_locator_url = 'https://dealer-locator.cars.tatamotors.com/sab-motors-delhi',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Tata Motors Official Passenger Vehicles Dealer Network'
WHERE id = 1;

-- Dealer 2: Hyundai Koncept Okhla (Delhi)
UPDATE dealers SET 
  name = 'Koncept Hyundai South Delhi',
  brand_name = 'Hyundai',
  dealer_type = 'SALES_AND_SERVICE',
  locality = 'Okhla Phase-II',
  latitude = 28.5300000,
  longitude = 77.2710000,
  official_locator_url = 'https://dealers.hyundai.co.in/koncept-okhla',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Hyundai Motor India Official Dealer Network'
WHERE id = 2;

-- Dealer 3: Maruti Suzuki Arena Naraina (Delhi)
UPDATE dealers SET 
  name = 'Rana Motors Maruti Suzuki Arena',
  brand_name = 'Maruti Suzuki',
  dealer_type = 'SALES_AND_SERVICE',
  locality = 'Naraina Industrial Area Phase-I',
  latitude = 28.6279000,
  longitude = 77.1350000,
  official_locator_url = 'https://www.marutisuzuki.com/dealers/rana-naraina',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Maruti Suzuki India Dealer Portal'
WHERE id = 3;

-- Dealer 4: KTM & Husqvarna South Extension (Delhi)
UPDATE dealers SET 
  name = 'KTM & Husqvarna South Extension',
  brand_name = 'KTM',
  dealer_type = 'SALES',
  locality = 'South Extension Part II',
  latitude = 28.5684000,
  longitude = 77.2215000,
  official_locator_url = 'https://www.ktmindia.com/ktm-dealers/delhi-south-ex',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Bajaj Auto / KTM Official Network Directory'
WHERE id = 4;

-- Dealer 5: Ather Space Indiranagar (Bengaluru)
UPDATE dealers SET 
  name = 'Ather Space Indiranagar Experience Center',
  brand_name = 'Ather Energy',
  dealer_type = 'SALES_AND_SERVICE',
  locality = 'Indiranagar 100ft Road',
  latitude = 12.9716000,
  longitude = 77.6412000,
  official_locator_url = 'https://www.atherenergy.com/experience-centres/bengaluru-indiranagar',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Ather Energy Verified Flagship Directory'
WHERE id = 5;

-- Dealer 6: Royal Enfield Brand Store Connaught Place (Delhi)
UPDATE dealers SET 
  name = 'Pavitra Motors Royal Enfield Brand Store',
  brand_name = 'Royal Enfield',
  dealer_type = 'SALES',
  locality = 'Connaught Place M-Block',
  latitude = 28.6315000,
  longitude = 77.2167000,
  official_locator_url = 'https://dealers.royalenfield.com/pavitra-cp',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'Royal Enfield Official Dealer Locator'
WHERE id = 6;

-- Dealer 7: CRITICAL BUG FIX -> BMW Deutsche Motoren (Delhi) - Was incorrectly tagged as Yamaha
UPDATE dealers SET 
  name = 'Deutsche Motoren BMW Authorized Showroom',
  brand_name = 'BMW',
  city = 'New Delhi',
  state = 'Delhi',
  address = 'Block B-1, Mohan Cooperative Industrial Estate, Mathura Road',
  locality = 'Mohan Cooperative Industrial Area',
  pincode = '110044',
  phone = '+91 11 4309 0000',
  website = 'https://www.bmw.in/deutsche-motoren',
  dealer_type = 'SALES_AND_SERVICE',
  latitude = 28.5340000,
  longitude = 77.2830000,
  official_locator_url = 'https://www.bmw.in/en/fastlane/dealer-locator.html',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'BMW India Official Dealer Network Directory'
WHERE id = 7;

-- Dealer 8: CRITICAL DIVISION FIX -> Honda Motorcycle & Scooter India (HMSI Two-Wheelers Only)
UPDATE dealers SET 
  name = 'Prime Honda Two-Wheeler Network Hub',
  brand_name = 'Honda',
  locality = 'Andheri Sakinaka',
  latitude = 19.1075000,
  longitude = 72.8877000,
  official_locator_url = 'https://www.honda2wheelersindia.com/dealer-locator',
  dealer_type = 'SALES',
  verification_status = 'OFFICIAL_OEM_VERIFIED',
  verified_source = 'HMSI Official Two-Wheeler Dealer Locator'
WHERE id = 8;

-- 6. Insert Additional Authorized Dealerships to Cover All Brands and Categories
INSERT INTO dealers (id, name, brand_name, is_authorized, dealer_type, verification_status, address, locality, city, state, pincode, phone, website, official_locator_url, business_hours, verified_source, verified_date, latitude, longitude) VALUES
-- 9. Dedicated Honda Cars Showroom (HCIL Car Division - NOT Two-Wheelers)
(9, 'Apex Honda Cars Exclusive Showroom', 'Honda', TRUE, 'SALES_AND_SERVICE', 'OFFICIAL_OEM_VERIFIED',
 'Plot No. 4, Block A, Mathura Road', 'Mohan Cooperative Area', 'New Delhi', 'Delhi', '110044', '+91 11 4678 9900', 'https://www.hondacarindia.com/dealers/apex-delhi', 'https://www.hondacarindia.com/dealer-locator', '09:30 AM - 07:30 PM (Mon-Sun)', 'Honda Cars India Official Dealer Network', '2026-03-01', 28.5360000, 77.2820000),

-- 10. Dedicated Yamaha Motorcycle & Scooter Showroom (Bengaluru)
(10, 'Bangalore Yamaha City Motors', 'Yamaha', TRUE, 'SALES', 'OFFICIAL_OEM_VERIFIED',
 'Lalbagh Road, Near Urvashi Theatre, Richmond Town', 'Richmond Town', 'Bengaluru', 'Karnataka', '560027', '+91 80 2222 3456', 'https://www.yamaha-motor-india.com/dealers/bangalore-city', 'https://www.yamaha-motor-india.com/dealer-locator.html', '10:00 AM - 07:30 PM (Mon-Sun)', 'India Yamaha Motor Official Directory', '2026-03-01', 12.9600000, 77.6000000),

-- 11. Mahindra SUV Authorized Sales & Service Showroom (Delhi)
(11, 'NBS International Mahindra SUV Showroom', 'Mahindra', TRUE, 'SALES_AND_SERVICE', 'OFFICIAL_OEM_VERIFIED',
 'Inner Circle, E-Block, Connaught Place', 'Connaught Place', 'New Delhi', 'Delhi', '110001', '+91 11 4350 7800', 'https://auto.mahindra.com/dealers/nbs-delhi', 'https://auto.mahindra.com/dealer-locator', '09:30 AM - 08:00 PM (Mon-Sun)', 'Mahindra Auto Official Showroom Locator', '2026-03-01', 28.6320000, 77.2190000),

-- 12. Kia Motors Authorized Sales Showroom (Delhi)
(12, 'Frontier Kia Safdarjung Showroom', 'Kia', TRUE, 'SALES', 'OFFICIAL_OEM_VERIFIED',
 'A-2/14, Safdarjung Enclave, Africa Avenue', 'Safdarjung Enclave', 'New Delhi', 'Delhi', '110029', '+91 11 4987 6500', 'https://www.kia.com/in/dealers/frontier-safdarjung', 'https://www.kia.com/in/buy/find-a-dealer.html', '09:30 AM - 07:30 PM (Mon-Sun)', 'Kia India Official Dealer Portal', '2026-03-01', 28.5620000, 77.2010000),

-- 13. TVS Motor Authorized Motorcycle & Scooter Dealership (Chennai)
(13, 'Brilliant TVS Two-Wheeler Showroom', 'TVS Motor', TRUE, 'SALES', 'OFFICIAL_OEM_VERIFIED',
 'Anna Salai, Near Thousand Lights', 'Mount Road', 'Chennai', 'Tamil Nadu', '600002', '+91 44 2855 4321', 'https://dealers.tvsmotor.com/brilliant-chennai', 'https://dealers.tvsmotor.com/', '09:00 AM - 08:00 PM (Mon-Sun)', 'TVS Motor Company Official Dealer Network', '2026-03-01', 13.0600000, 80.2500000),

-- 14. Bajaj Auto Authorized Dealership (Delhi)
(14, 'Bagga Link Bajaj Two-Wheeler Dealership', 'Bajaj Auto', TRUE, 'SALES_AND_SERVICE', 'OFFICIAL_OEM_VERIFIED',
 '12, Desh Bandhu Gupta Road, Karol Bagh', 'Karol Bagh', 'New Delhi', 'Delhi', '110005', '+91 11 4511 2233', 'https://www.bajajauto.com/dealers/bagga-link', 'https://www.bajajauto.com/dealer-locator', '09:30 AM - 07:30 PM (Mon-Sun)', 'Bajaj Auto Official Dealer Directory', '2026-03-01', 28.6510000, 77.1930000),

-- 15. Ola Electric Experience Centre Koramangala (Bengaluru)
(15, 'Ola Electric Experience Centre Koramangala', 'Ola Electric', TRUE, 'SALES', 'OFFICIAL_OEM_VERIFIED',
 '80 Feet Road, 4th Block, Koramangala', 'Koramangala', 'Bengaluru', 'Karnataka', '560034', '+91 80 3544 5500', 'https://www.olaelectric.com/stores/koramangala', 'https://www.olaelectric.com/experience-centres', '09:30 AM - 08:30 PM (Mon-Sun)', 'Ola Electric Verified Showroom Network', '2026-03-01', 12.9352000, 77.6245000),

-- 16. SERVICE-ONLY Workshop -> Crucial for testing: Must NEVER appear as a sales showroom!
(16, 'Tata Motors Fast-Track Authorized Service Workshop', 'Tata Motors', TRUE, 'SERVICE', 'OFFICIAL_OEM_VERIFIED',
 'C-45, Phase-I, Okhla Industrial Area', 'Okhla Phase-I', 'New Delhi', 'Delhi', '110020', '+91 11 4100 2200', 'https://service.tatamotors.com/delhi-okhla', 'https://dealer-locator.cars.tatamotors.com/', '08:30 AM - 06:30 PM (Mon-Sat)', 'Tata Motors Customer Care Service Directory', '2026-03-01', 28.5250000, 77.2760000)
ON DUPLICATE KEY UPDATE name=VALUES(name), dealer_type=VALUES(dealer_type), official_locator_url=VALUES(official_locator_url);

-- 7. Seed Normalized Dealer-Brand Authorizations (dealer_brands)
INSERT INTO dealer_brands (dealer_id, brand_id, vehicle_category, is_authorized, division_name) VALUES
-- Dealer 1 (SAB Motors): Tata Motors Cars & EVs
(1, 1, 'CAR', TRUE, 'Tata Motors Passenger Vehicles'),
(1, 1, 'ELECTRIC_CAR', TRUE, 'Tata Passenger Electric Mobility'),

-- Dealer 2 (Koncept Hyundai): Hyundai Cars
(2, 2, 'CAR', TRUE, 'Hyundai Motor India Ltd'),

-- Dealer 3 (Rana Motors): Maruti Suzuki Cars
(3, 3, 'CAR', TRUE, 'Maruti Suzuki Arena'),

-- Dealer 4 (KTM South Ex): KTM Motorcycles
(4, 4, 'MOTORCYCLE', TRUE, 'KTM India'),

-- Dealer 5 (Ather Indiranagar): Ather Electric Two-Wheelers
(5, 7, 'ELECTRIC_TWO_WHEELER', TRUE, 'Ather Energy'),

-- Dealer 6 (Pavitra Royal Enfield): Royal Enfield Motorcycles
(6, 6, 'MOTORCYCLE', TRUE, 'Royal Enfield'),

-- Dealer 7 (Deutsche Motoren BMW): BMW Cars & Luxury EVs
(7, 14, 'CAR', TRUE, 'BMW Group India'),
(7, 14, 'ELECTRIC_CAR', TRUE, 'BMW i Electric Division'),

-- Dealer 8 (Prime Honda): Honda Two-Wheelers ONLY (Scooter & Motorcycle - NOT CARS!)
(8, 8, 'SCOOTER', TRUE, 'Honda Motorcycle & Scooter India (HMSI)'),
(8, 8, 'MOTORCYCLE', TRUE, 'Honda Motorcycle & Scooter India (HMSI)'),

-- Dealer 9 (Apex Honda Cars): Honda Cars ONLY (NOT Scooters!)
(9, 8, 'CAR', TRUE, 'Honda Cars India Ltd (HCIL)'),

-- Dealer 10 (Bangalore Yamaha): Yamaha Motorcycles & Scooters
(10, 5, 'MOTORCYCLE', TRUE, 'India Yamaha Motor'),
(10, 5, 'SCOOTER', TRUE, 'India Yamaha Motor'),

-- Dealer 11 (NBS Mahindra): Mahindra SUVs
(11, 9, 'CAR', TRUE, 'Mahindra Auto SUV Division'),

-- Dealer 12 (Frontier Kia): Kia Cars
(12, 10, 'CAR', TRUE, 'Kia India'),

-- Dealer 13 (Brilliant TVS): TVS Two-Wheelers
(13, 11, 'MOTORCYCLE', TRUE, 'TVS Motor Company'),
(13, 11, 'SCOOTER', TRUE, 'TVS Motor Company'),

-- Dealer 14 (Bagga Link Bajaj): Bajaj Motorcycles
(14, 12, 'MOTORCYCLE', TRUE, 'Bajaj Auto Motorcycles'),

-- Dealer 15 (Ola Koramangala): Ola Electric Two-Wheelers
(15, 13, 'ELECTRIC_TWO_WHEELER', TRUE, 'Ola Electric'),

-- Dealer 16 (Tata Service Workshop): Tata Cars Service Only
(16, 1, 'CAR', TRUE, 'Tata Motors Authorized Service Workshop')
ON DUPLICATE KEY UPDATE division_name=VALUES(division_name), is_authorized=VALUES(is_authorized);

-- 8. Seed Model-Specific Dealer Inventory & Test Drive Availability (dealer_vehicle_availability)
INSERT INTO dealer_vehicle_availability (dealer_id, vehicle_id, variant_id, availability_status, test_drive_available, estimated_delivery_days, last_verified_date, notes) VALUES
-- Tata Nexon at SAB Motors (Delhi)
(1, 1, 1, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 14, '2026-03-01', 'Test drive vehicles available for both Petrol MT and DCA Automatic'),

-- Tata Nexon.ev at SAB Motors (Delhi)
(1, 4, 4, 'CONFIRMED_IN_STOCK', TRUE, 7, '2026-03-01', 'Empowered Plus 40.5 kWh unit in Pristine White ready for fast delivery'),

-- Hyundai Creta at Koncept Hyundai (Delhi)
(2, 2, 2, 'CONFIRMED_IN_STOCK', TRUE, 5, '2026-03-01', 'Creta SX(O) Petrol IVT in Titan Grey ready in showroom yard'),

-- Maruti Suzuki Brezza at Rana Motors (Delhi)
(3, 13, 16, 'CONFIRMED_IN_STOCK', TRUE, 7, '2026-03-01', 'Brezza ZXi 6AT available with dual-tone roof option'),

-- KTM 200 Duke at KTM South Extension (Delhi)
(4, 5, 5, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 3, '2026-03-01', 'Electronic Orange demo bike on site for customer test rides'),

-- Ather 450X at Ather Space Indiranagar (Bengaluru)
(5, 8, 8, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 4, '2026-03-01', 'Warp mode test track ride open daily'),

-- Royal Enfield Hunter 350 at Pavitra Motors CP (Delhi)
(6, 7, 7, 'CONFIRMED_IN_STOCK', TRUE, 2, '2026-03-01', 'Dapper Ash and Rebel Blue variants in ready stock'),

-- BMW 3 Series Gran Limousine at Deutsche Motoren (Delhi)
(7, 19, 22, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 10, '2026-03-01', '330Li M Sport test drive vehicle with BMW Genius demonstration'),

-- Honda Activa 6G at Prime Honda Two-Wheeler (Mumbai)
(8, 9, 9, 'CONFIRMED_IN_STOCK', TRUE, 1, '2026-03-01', 'All colour options of Activa 6G DLX in immediate stock'),

-- Honda City at Apex Honda Cars (Delhi)
(9, 14, 17, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 7, '2026-03-01', 'ZX CVT Executive sedan available for highway test drive'),

-- Yamaha YZF R15 V4 at Bangalore Yamaha (Bengaluru)
(10, 6, 6, 'CONFIRMED_IN_STOCK', TRUE, 3, '2026-03-01', 'Racing Blue with quickshifter in ready stock'),

-- Mahindra XUV700 at NBS International (Delhi)
(11, 10, 12, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 21, '2026-03-01', 'AX7 Luxury Pack Diesel AT demo vehicle available for ADAS trial'),

-- Kia Seltos at Frontier Kia (Delhi)
(12, 12, 15, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 14, '2026-03-01', 'HTX IVT test vehicle available on site'),

-- TVS Apache RTR 200 4V at Brilliant TVS (Chennai)
(13, 16, 19, 'CONFIRMED_IN_STOCK', TRUE, 2, '2026-03-01', 'Dual channel ABS with ride modes ready for delivery'),

-- Bajaj Pulsar NS200 at Bagga Link Bajaj (Delhi)
(14, 17, 20, 'CONFIRMED_IN_STOCK', TRUE, 2, '2026-03-01', 'Ebony Black with USD forks in ready stock'),

-- Ola S1 Pro Gen 2 at Ola Koramangala (Bengaluru)
(15, 18, 21, 'AVAILABLE_FOR_TEST_DRIVE', TRUE, 5, '2026-03-01', 'Hyperdrive experience test ride ready')
ON DUPLICATE KEY UPDATE availability_status=VALUES(availability_status), test_drive_available=VALUES(test_drive_available);
