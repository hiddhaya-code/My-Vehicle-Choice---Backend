-- ============================================================================
-- MyVehicleChoice Database Migration: V1 Initial Schema
-- Target: MySQL 8.0+
-- ============================================================================

-- 1. Brands Table
CREATE TABLE IF NOT EXISTS brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    logo_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_brand_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Vehicles (Models) Table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    model_name VARCHAR(150) NOT NULL,
    category ENUM('CAR', 'MOTORCYCLE', 'SCOOTER', 'ELECTRIC_CAR', 'ELECTRIC_TWO_WHEELER') NOT NULL,
    body_style VARCHAR(50),
    headline VARCHAR(255),
    description TEXT,
    hero_image_url VARCHAR(500),
    is_trending BOOLEAN DEFAULT FALSE,
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_brand FOREIGN KEY (brand_id) REFERENCES brands (id) ON DELETE RESTRICT,
    CONSTRAINT uk_brand_model UNIQUE (brand_id, model_name),
    INDEX idx_vehicle_category (category),
    INDEX idx_vehicle_trending (is_trending),
    INDEX idx_vehicle_featured (is_featured)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Vehicle Variants Table
CREATE TABLE IF NOT EXISTS vehicle_variants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    variant_name VARCHAR(150) NOT NULL,
    model_year INT NOT NULL,
    fuel_type ENUM('PETROL', 'DIESEL', 'CNG', 'HYBRID', 'ELECTRIC') NOT NULL,
    transmission ENUM('MANUAL', 'AUTOMATIC') NOT NULL,
    ex_showroom_price DECIMAL(12,2) NOT NULL,
    estimated_on_road_price DECIMAL(12,2) NOT NULL,
    city_price_ref VARCHAR(50) DEFAULT 'New Delhi',
    mileage_kmpl DECIMAL(5,2) NULL,
    electric_range_km INT NULL,
    battery_capacity_kwh DECIMAL(6,2) NULL,
    engine_cc INT NULL,
    max_power_bhp DECIMAL(6,2) NULL,
    max_power_rpm INT NULL,
    max_torque_nm DECIMAL(6,2) NULL,
    seating_capacity INT NULL,
    boot_space_litres INT NULL,
    ground_clearance_mm INT NULL,
    fuel_tank_capacity_litres DECIMAL(5,2) NULL,
    kerb_weight_kg INT NULL,
    seat_height_mm INT NULL,
    safety_rating_stars INT NULL,
    safety_rating_protocol VARCHAR(50) NULL,
    airbags_count INT NULL,
    has_abs BOOLEAN DEFAULT TRUE,
    has_esp BOOLEAN DEFAULT FALSE,
    maintenance_estimate_annual DECIMAL(10,2) NULL,
    key_advantages TEXT NULL,
    key_disadvantages TEXT NULL,
    is_verified BOOLEAN DEFAULT TRUE,
    data_source VARCHAR(150) DEFAULT 'Manufacturer Official Specs (India)',
    verified_date DATE NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_variant_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE CASCADE,
    CONSTRAINT uk_vehicle_variant_year UNIQUE (vehicle_id, variant_name, model_year),
    INDEX idx_variant_price (estimated_on_road_price),
    INDEX idx_variant_fuel (fuel_type),
    INDEX idx_variant_transmission (transmission)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Vehicle Images Table
CREATE TABLE IF NOT EXISTS vehicle_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    variant_id BIGINT NULL,
    image_url VARCHAR(500) NOT NULL,
    caption VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_image_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE CASCADE,
    CONSTRAINT fk_image_variant FOREIGN KEY (variant_id) REFERENCES vehicle_variants (id) ON DELETE SET NULL,
    INDEX idx_image_vehicle (vehicle_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Data Sources Table
CREATE TABLE IF NOT EXISTS data_sources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_name VARCHAR(150) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    base_url VARCHAR(500),
    verification_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_data_source_name UNIQUE (source_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
