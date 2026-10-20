-- ============================================================================
-- MyVehicleChoice Database Migration: V2 Seed Catalogue Data
-- High-quality, verified baseline demonstration dataset for Indian market
-- ============================================================================

-- 1. Insert Brands
INSERT INTO brands (id, name, country, logo_url, is_active) VALUES
(1, 'Tata Motors', 'India', 'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=120&q=80', TRUE),
(2, 'Hyundai', 'South Korea', 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=120&q=80', TRUE),
(3, 'Maruti Suzuki', 'India', 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=120&q=80', TRUE),
(4, 'KTM', 'Austria', 'https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=120&q=80', TRUE),
(5, 'Yamaha', 'Japan', 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=120&q=80', TRUE),
(6, 'Royal Enfield', 'India', 'https://images.unsplash.com/photo-1609630875171-b1321377ee65?auto=format&fit=crop&w=120&q=80', TRUE),
(7, 'Ather Energy', 'India', 'https://images.unsplash.com/photo-1558980664-3a031cf67ea8?auto=format&fit=crop&w=120&q=80', TRUE),
(8, 'Honda', 'Japan', 'https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=120&q=80', TRUE);

-- 2. Insert Data Sources
INSERT INTO data_sources (id, source_name, source_type, base_url, verification_date, notes) VALUES
(1, 'Society of Indian Automobile Manufacturers (SIAM)', 'INDUSTRY_BODY', 'https://www.siam.in', '2026-01-15', 'Official industry fuel efficiency and safety certification records'),
(2, 'Bharat NCAP & Global NCAP Safety Test Database', 'SAFETY_AGENCY', 'https://www.globalncap.org', '2026-02-10', 'Crash test ratings and occupant protection assessments'),
(3, 'Official OEM Indian Spec Sheets & Ex-Showroom Tariffs', 'MANUFACTURER', 'https://www.tatamotors.com', '2026-03-01', 'Direct verified ex-showroom price lists and technical specifications');

-- 3. Insert Vehicles
INSERT INTO vehicles (id, brand_id, model_name, category, body_style, headline, description, hero_image_url, is_trending, is_featured) VALUES
-- Car 1: Tata Nexon
(1, 1, 'Nexon', 'CAR', 'SUV', '5-Star B-NCAP Safety with Futuristic Tech & Turbo Performance',
 'The Tata Nexon is India’s premier compact SUV known for uncompromising build quality, advanced dual-screen digital cockpit, and punchy turbo powertrains.',
 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Car 2: Hyundai Creta
(2, 2, 'Creta', 'CAR', 'SUV', 'The Benchmark Mid-Size SUV with Panoramic Sunroof & ADAS Level 2',
 'Hyundai Creta leads the Indian mid-size SUV segment with segment-defining comfort, refined petrol and diesel powertrains, and cutting-edge connected car features.',
 'https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Car 3: Maruti Suzuki Swift
(3, 3, 'Swift', 'CAR', 'HATCHBACK', 'Agile, Fun-to-Drive Hatchback with Incredible Mileage',
 'The all-new Swift brings sharp sporty styling, an efficient Z-Series petrol engine, 6 standard airbags, and exceptional fuel economy for everyday urban agility.',
 'https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=900&q=80', TRUE, FALSE),

-- EV Car: Tata Nexon EV
(4, 1, 'Nexon EV', 'ELECTRIC_CAR', 'SUV', 'India’s Most Popular Electric SUV with 465 km Real-World Range',
 'Equipped with liquid-cooled high-capacity IP67 battery, V2V and V2L charging capabilities, and rapid DC fast-charging for seamless highway touring.',
 'https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Motorcycle 1: KTM 200 Duke
(5, 4, '200 Duke', 'MOTORCYCLE', 'SPORT', 'The Ultimate 200cc Street Fighter with Razor-Sharp Dynamics',
 'Lightweight trellis frame, inverted WP Apex suspension, and a rev-happy liquid-cooled DOHC engine delivering an intoxicating power-to-weight ratio.',
 'https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Motorcycle 2: Yamaha YZF R15 V4
(6, 5, 'YZF R15 V4', 'MOTORCYCLE', 'SPORT', 'MotoGP Inspired Track Weapon with Variable Valve Actuation (VVA)',
 'Aerodynamic M-style fairing, bi-functional LED projector headlight, traction control system, and assist & slipper clutch for track enthusiasts.',
 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Motorcycle 3: Royal Enfield Hunter 350
(7, 6, 'Hunter 350', 'MOTORCYCLE', 'COMMUTER', 'Modern Urban Roadster with Iconic J-Series Character',
 'Compact geometry, torquey 349cc single cylinder motor, throaty exhaust note, and easy flickability for city commutes and weekend getaways.',
 'https://images.unsplash.com/photo-1609630875171-b1321377ee65?auto=format&fit=crop&w=900&q=80', TRUE, FALSE),

-- Electric Scooter: Ather 450X
(8, 7, '450X', 'ELECTRIC_TWO_WHEELER', 'COMMUTER', 'Smart Performance Electric Scooter with Warp Mode & Google Maps',
 'High-tensile aluminium frame, blistering 0-40 in 3.3s, Google Maps navigation with live dashboard routing, and ultra-reliable battery architecture.',
 'https://images.unsplash.com/photo-1558980664-3a031cf67ea8?auto=format&fit=crop&w=900&q=80', TRUE, TRUE),

-- Scooter: Honda Activa 6G
(9, 8, 'Activa 6G', 'SCOOTER', 'COMMUTER', 'India’s Most Trusted Family Scooter with eSP Technology',
 'Silent start ACG motor, metal body durability, external fuel filling cap, telescopic suspension, and unparalleled resale value across India.',
 'https://images.unsplash.com/photo-1558981408-db0ecd8a1ee4?auto=format&fit=crop&w=900&q=80', FALSE, TRUE);

-- 4. Insert Vehicle Variants
INSERT INTO vehicle_variants (
    id, vehicle_id, variant_name, model_year, fuel_type, transmission,
    ex_showroom_price, estimated_on_road_price, city_price_ref,
    mileage_kmpl, electric_range_km, battery_capacity_kwh,
    engine_cc, max_power_bhp, max_power_rpm, max_torque_nm,
    seating_capacity, boot_space_litres, ground_clearance_mm, fuel_tank_capacity_litres,
    kerb_weight_kg, seat_height_mm, safety_rating_stars, safety_rating_protocol,
    airbags_count, has_abs, has_esp, maintenance_estimate_annual,
    key_advantages, key_disadvantages, is_verified, data_source, verified_date
) VALUES
-- Tata Nexon Pure 1.2 Petrol Manual
(1, 1, 'Pure 1.2 Revotron Petrol 6MT', 2026, 'PETROL', 'MANUAL',
 979000.00, 1115000.00, 'New Delhi',
 17.44, NULL, NULL,
 1199, 118.2, 5500, 170.0,
 5, 382, 208, 44.0,
 1255, NULL, 5, 'Bharat NCAP',
 6, TRUE, TRUE, 7500.00,
 '5-Star crash safety rating, high ground clearance of 208mm, 6 standard airbags, robust ride quality over potholes',
 'City mileage drops in heavy bumper-to-bumper traffic; 3-cylinder engine vibrates slightly at idle',
 TRUE, 'Tata Motors Official Brochure 2026', '2026-02-15'),

-- Tata Nexon Creative Plus 1.5 Diesel Automatic
(2, 1, 'Creative Plus 1.5 Revotorq Diesel 6AMT', 2026, 'DIESEL', 'AUTOMATIC',
 1320000.00, 1530000.00, 'New Delhi',
 23.20, NULL, NULL,
 1497, 113.4, 3750, 260.0,
 5, 382, 208, 44.0,
 1320, NULL, 5, 'Bharat NCAP',
 6, TRUE, TRUE, 9200.00,
 'Remarkable 260 Nm torque, stellar 23+ kmpl highway fuel efficiency, paddle shifters on AMT, high ground clearance',
 'AMT gearbox has noticeable shift lag during aggressive overtakes compared to dual-clutch boxes',
 TRUE, 'Tata Motors Official Brochure 2026', '2026-02-15'),

-- Hyundai Creta SX 1.5 MPi Petrol Manual
(3, 2, 'SX 1.5 Petrol 6MT', 2026, 'PETROL', 'MANUAL',
 1530000.00, 1765000.00, 'New Delhi',
 17.40, NULL, NULL,
 1497, 113.0, 6300, 143.8,
 5, 433, 190, 50.0,
 1290, NULL, 5, 'Bharat NCAP',
 6, TRUE, TRUE, 8500.00,
 'Segment-leading refinement, panoramic sunroof, spacious 433L boot, plush rear-seat thigh support',
 'Naturally aspirated petrol lacks mid-range turbo surge on steep mountain climbs with full load',
 TRUE, 'Hyundai India Price List 2026', '2026-03-01'),

-- Hyundai Creta SX(O) 1.5 CRDi Diesel Automatic
(4, 2, 'SX(O) 1.5 Diesel 6AT (ADAS)', 2026, 'DIESEL', 'AUTOMATIC',
 2015000.00, 2340000.00, 'New Delhi',
 19.10, NULL, NULL,
 1493, 114.4, 4000, 250.0,
 5, 433, 190, 50.0,
 1360, NULL, 5, 'Bharat NCAP',
 6, TRUE, TRUE, 10500.00,
 'Level 2 ADAS suite, silky smooth torque-converter 6AT gearbox, ventilated front seats, premium Bose 8-speaker audio',
 'Pricier top variant with higher road tax in major metropolitan cities',
 TRUE, 'Hyundai India Price List 2026', '2026-03-01'),

-- Maruti Suzuki Swift ZXi 1.2 Petrol Manual
(5, 3, 'ZXi 1.2 Z-Series Petrol 5MT', 2026, 'PETROL', 'MANUAL',
 830000.00, 940000.00, 'New Delhi',
 24.80, NULL, NULL,
 1197, 80.4, 5700, 111.7,
 5, 265, 163, 37.0,
 920, NULL, 4, 'Global NCAP',
 6, TRUE, TRUE, 5500.00,
 'Phenomenal 24.8 kmpl ARAI mileage, lowest annual maintenance cost, extensive pan-India service network',
 'Modest boot space of 265L, lighter body feel on triple-digit highway speeds',
 TRUE, 'Maruti Suzuki Technical Spec 2026', '2026-02-10'),

-- Tata Nexon EV Empowered Plus Long Range
(6, 4, 'Empowered Plus 45 Long Range', 2026, 'ELECTRIC', 'AUTOMATIC',
 1699000.00, 1785000.00, 'New Delhi',
 NULL, 465, 45.00,
 NULL, 142.7, NULL, 215.0,
 5, 350, 205, NULL,
 1450, NULL, 5, 'Bharat NCAP',
 6, TRUE, TRUE, 4500.00,
 'Zero tailpipe emissions, ultra-low running cost (~₹1.10/km), 465 km certified range, 0-100 in 8.9s, V2L vehicle-to-load feature',
 'Highway fast charger network still growing in remote tier-3 sectors; cold weather reduces real range by 10-15%',
 TRUE, 'Tata Passenger Electric Mobility 2026', '2026-02-28'),

-- KTM 200 Duke Standard Dual-Channel ABS
(7, 5, '200 Duke Dual-Channel ABS', 2026, 'PETROL', 'MANUAL',
 199000.00, 232000.00, 'New Delhi',
 33.00, NULL, NULL,
 199, 24.6, 10000, 19.3,
 2, NULL, 155, 13.4,
 159, 822, NULL, NULL,
 0, TRUE, FALSE, 4200.00,
 'Explosive top-end power delivery, WP upside-down forks, Supermoto ABS mode, aggressive trellis styling',
 'Aggressive riding posture causes wrist fatigue in slow commuter traffic; relatively firm pillion seat',
 TRUE, 'Bajaj Auto / KTM Official Spec 2026', '2026-01-20'),

-- Yamaha YZF R15 V4 Racing Blue
(8, 6, 'YZF R15 V4 Racing Blue (Quickshifter)', 2026, 'PETROL', 'MANUAL',
 187000.00, 218000.00, 'New Delhi',
 45.00, NULL, NULL,
 155, 18.1, 10000, 14.2,
 2, NULL, 170, 11.0,
 142, 815, NULL, NULL,
 0, TRUE, FALSE, 3800.00,
 'Clutchless upshifts via factory Quickshifter, Deltabox aluminium frame, extraordinary 45+ kmpl mileage, Traction Control',
 'Committed supersport clip-on posture demanding for daily city traffic; firm seat cushion',
 TRUE, 'India Yamaha Motor Spec Sheet 2026', '2026-01-25'),

-- Royal Enfield Hunter 350 Dapper
(9, 7, 'Hunter 350 Dapper Series', 2026, 'PETROL', 'MANUAL',
 169000.00, 196000.00, 'New Delhi',
 36.20, NULL, NULL,
 349, 20.2, 6100, 27.0,
 2, NULL, 150, 13.0,
 181, 790, NULL, NULL,
 0, TRUE, FALSE, 4000.00,
 'Accessible 790mm seat height, low-end torque rumble, nimble 17-inch alloy wheels, iconic British-heritage roadster design',
 'Rear suspension is slightly stiff over sharp speed bumps; basic instrument cluster without turn-by-turn standard',
 TRUE, 'Royal Enfield India 2026', '2026-02-05'),

-- Ather 450X Gen 3 3.7 kWh
(10, 8, '450X 3.7 kWh with Pro Pack', 2026, 'ELECTRIC', 'AUTOMATIC',
 145000.00, 158000.00, 'New Delhi',
 NULL, 150, 3.70,
 NULL, 8.5, NULL, 26.0,
 2, NULL, 153, NULL,
 111, 780, NULL, NULL,
 0, FALSE, FALSE, 2500.00,
 'Snap acceleration in Warp mode, Google Maps on 7-inch waterproof touchscreen, park assist reverse mode, warp-fast charging',
 'Initial purchase price is higher than conventional 110cc petrol scooters; depends on home charging plug',
 TRUE, 'Ather Energy Spec Portal 2026', '2026-02-18'),

-- Honda Activa 6G DLX
(11, 9, 'Activa 6G Deluxe', 2026, 'PETROL', 'AUTOMATIC',
 79000.00, 92000.00, 'New Delhi',
 50.00, NULL, NULL,
 109, 7.7, 8000, 8.9,
 2, NULL, 162, 5.3,
 106, 765, NULL, NULL,
 0, FALSE, FALSE, 2200.00,
 'Bulletproof Honda eSP engine, hassle-free CVT automatic, metal body durability, highest resale value in Indian 2W market',
 'Analogue speedometer lacks modern digital readout, telescopic suspension tuned on softer side',
 TRUE, 'Honda Motorcycle & Scooter India 2026', '2026-01-10');

-- 5. Insert Vehicle Images
INSERT INTO vehicle_images (id, vehicle_id, variant_id, image_url, caption, is_primary, display_order) VALUES
(1, 1, 1, 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=900&q=80', 'Tata Nexon Front Three-Quarter View', TRUE, 1),
(2, 2, 3, 'https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=900&q=80', 'Hyundai Creta Front Grille and LED Headlamps', TRUE, 1),
(3, 3, 5, 'https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?auto=format&fit=crop&w=900&q=80', 'Maruti Suzuki Swift Urban Hatchback Profile', TRUE, 1),
(4, 4, 6, 'https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=900&q=80', 'Tata Nexon EV Clean Zero-Emission Crossover', TRUE, 1),
(5, 5, 7, 'https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?auto=format&fit=crop&w=900&q=80', 'KTM 200 Duke Trellis Frame and Sharp Fairing', TRUE, 1),
(6, 6, 8, 'https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=900&q=80', 'Yamaha YZF R15 V4 Aerodynamic Track Silhouette', TRUE, 1),
(7, 7, 9, 'https://images.unsplash.com/photo-1609630875171-b1321377ee65?auto=format&fit=crop&w=900&q=80', 'Royal Enfield Hunter 350 Dapper Dual-Tone Tank', TRUE, 1),
(8, 8, 10, 'https://images.unsplash.com/photo-1558980664-3a031cf67ea8?auto=format&fit=crop&w=900&q=80', 'Ather 450X Gen 3 Sleek Futuristic Chassis', TRUE, 1),
(9, 9, 11, 'https://images.unsplash.com/photo-1558981408-db0ecd8a1ee4?auto=format&fit=crop&w=900&q=80', 'Honda Activa 6G Metal Body Classic Commuter', TRUE, 1);
