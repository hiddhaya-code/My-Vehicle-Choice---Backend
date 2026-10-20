-- ============================================================================
-- MyVehicleChoice Database Migration: V5 Add BMW & Correct Mahindra XUV700 Image
-- ============================================================================

-- 1. Correct Mahindra XUV700 (ID 10) Hero Image from Lamborghini to authentic SUV
UPDATE vehicles SET 
  hero_image_url = 'https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=1200&q=80'
WHERE id = 10;

-- 2. Insert BMW Brand
INSERT INTO brands (id, name, country, is_active) VALUES
(14, 'BMW', 'Germany', TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name), is_active=VALUES(is_active);

-- 3. Insert BMW 3 Series Gran Limousine (Vehicle ID 19)
INSERT INTO vehicles (id, brand_id, model_name, category, body_style, headline, description, hero_image_url, is_trending, is_featured) VALUES
(19, 14, '3 Series Gran Limousine', 'CAR', 'SEDAN', 
 'The Ultimate Luxury Sports Sedan with Extended Limousine Wheelbase & 258 bhp Turbo', 
 'BMW''s best-selling luxury sedan in India combining thrilling 50:50 driving dynamics, panoramic glass sunroof, BMW Curved Display with OS 8, and executive rear-seat legroom.', 
 'https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=1200&q=80', TRUE, TRUE)
ON DUPLICATE KEY UPDATE model_name=VALUES(model_name), headline=VALUES(headline), hero_image_url=VALUES(hero_image_url);

-- 4. Insert Verified BMW 330Li M Sport Variant (Variant ID 22)
INSERT INTO vehicle_variants (
  id, vehicle_id, variant_name, model_year, fuel_type, transmission, 
  ex_showroom_price, estimated_on_road_price, city_price_ref, 
  mileage_kmpl, electric_range_km, battery_capacity_kwh, engine_cc, max_power_bhp, max_power_rpm, max_torque_nm, 
  seating_capacity, boot_space_litres, ground_clearance_mm, fuel_tank_capacity_litres, kerb_weight_kg, seat_height_mm, 
  safety_rating_stars, safety_rating_protocol, airbags_count, has_abs, has_esp, maintenance_estimate_annual, 
  key_advantages, key_disadvantages, is_verified, data_source, verified_date
) VALUES
(22, 19, '330Li M Sport Gran Limousine', 2026, 'PETROL', 'AUTOMATIC',
 6060000.00, 6980000.00, 'New Delhi',
 15.39, NULL, NULL, 1998, 258.00, 5000, 400.00,
 5, 480, 140, 59.00, 1605, NULL,
 5, 'Euro NCAP', 6, TRUE, TRUE, 28000.00,
 'Exhilarating 258 bhp TwinPower Turbo acceleration (0-100 in 6.2s), 50:50 axle weight balance, executive rear legroom, 14.9-inch curved infotainment display',
 'Low 140mm ground clearance and low-profile runflat tyres demand care over large road speed breakers', TRUE, 'BMW India Official Brochure 2026', '2026-03-01')
ON DUPLICATE KEY UPDATE variant_name=VALUES(variant_name), estimated_on_road_price=VALUES(estimated_on_road_price);

-- 5. Insert Authorized BMW Dealership in Delhi
INSERT INTO dealers (id, name, brand_name, is_authorized, address, city, state, pincode, phone, website, business_hours, verified_source, verified_date) VALUES
(7, 'Deutsche Motoren BMW Authorized Showroom', 'BMW', TRUE, 'Block B-1, Mohan Cooperative Industrial Estate, Mathura Road', 'New Delhi', 'Delhi', '110044', '+91 11 4309 0000', 'https://www.bmw.in/deutsche-motoren', '09:30 AM - 07:00 PM (Mon-Sun)', 'BMW India Official Dealer Network', '2026-03-01')
ON DUPLICATE KEY UPDATE name=VALUES(name), brand_name=VALUES(brand_name);
