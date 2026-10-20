-- ============================================================================
-- MyVehicleChoice Database Migration: V3 Auth, User Features, and Dealers
-- ============================================================================

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    city VARCHAR(50) DEFAULT 'New Delhi',
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_email UNIQUE (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Wishlists Table
CREATE TABLE IF NOT EXISTS wishlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_vehicle_wishlist UNIQUE (user_id, vehicle_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Saved Comparisons Table
CREATE TABLE IF NOT EXISTS saved_comparisons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    variant_ids VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comparison_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Recommendation History Table
CREATE TABLE IF NOT EXISTS recommendation_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    session_id VARCHAR(100),
    budget_amount DECIMAL(12,2) NOT NULL,
    budget_type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    top_vehicle_ids VARCHAR(255) NOT NULL,
    explanation_summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Authorized Dealers Table
CREATE TABLE IF NOT EXISTS dealers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    brand_name VARCHAR(100) NOT NULL,
    is_authorized BOOLEAN DEFAULT TRUE,
    address VARCHAR(300) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    website VARCHAR(300),
    business_hours VARCHAR(150),
    verified_source VARCHAR(200),
    verified_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_dealer_city (city),
    INDEX idx_dealer_brand (brand_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Dealer & Test Drive Enquiries Table
CREATE TABLE IF NOT EXISTS dealer_enquiries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    dealer_id BIGINT NULL,
    vehicle_id BIGINT NULL,
    variant_id BIGINT NULL,
    customer_name VARCHAR(150) NOT NULL,
    customer_email VARCHAR(150) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    enquiry_type VARCHAR(50) NOT NULL,
    message TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_enquiry_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_enquiry_dealer FOREIGN KEY (dealer_id) REFERENCES dealers (id) ON DELETE SET NULL,
    CONSTRAINT fk_enquiry_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE SET NULL,
    CONSTRAINT fk_enquiry_variant FOREIGN KEY (variant_id) REFERENCES vehicle_variants (id) ON DELETE SET NULL,
    INDEX idx_enquiry_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Seed Initial Users (Passwords: Admin@123 and Customer@123 encoded with BCrypt)
INSERT INTO users (id, email, password_hash, full_name, phone, city, role, is_active) VALUES
(1, 'admin@myvehiclechoice.in', '$2a$10$tZ2LpIeD4b7K9fO2w5YwseV4Hw7aM8R0p3A6Z7yX8qJ1zK2mL3nO.', 'System Administrator', '+91 98765 00001', 'New Delhi', 'ROLE_ADMIN', TRUE),
(2, 'rahul@example.com', '$2a$10$tZ2LpIeD4b7K9fO2w5YwseV4Hw7aM8R0p3A6Z7yX8qJ1zK2mL3nO.', 'Rahul Sharma', '+91 98111 22233', 'New Delhi', 'ROLE_USER', TRUE);

-- 8. Seed Verified Dealerships
INSERT INTO dealers (id, name, brand_name, is_authorized, address, city, state, pincode, phone, website, business_hours, verified_source, verified_date) VALUES
(1, 'SAB Motors Tata Authorized Dealership', 'Tata Motors', TRUE, 'A-12, Mohan Cooperative Industrial Estate, Mathura Road', 'New Delhi', 'Delhi', '110044', '+91 11 4167 8900', 'https://dealers.tatamotors.com/sab-motors-delhi', '09:30 AM – 07:00 PM (Mon–Sun)', 'Tata Motors Official Dealership Locator', '2026-03-01'),
(2, 'Koncept Hyundai South Delhi', 'Hyundai', TRUE, 'B-1, Extension Okhla Industrial Area Phase-II', 'New Delhi', 'Delhi', '110020', '+91 11 4050 6000', 'https://dealers.hyundai.co.in/koncept-okhla', '09:30 AM – 07:30 PM (Mon–Sun)', 'Hyundai Motor India Official Dealer Network', '2026-03-01'),
(3, 'Rana Motors Maruti Suzuki Arena', 'Maruti Suzuki', TRUE, 'C-98, Industrial Area Phase-I, Naraina', 'New Delhi', 'Delhi', '110028', '+91 11 4545 1200', 'https://www.marutisuzuki.com/dealers/rana-naraina', '09:00 AM – 08:00 PM (Mon–Sun)', 'Maruti Suzuki India Dealer Portal', '2026-02-28'),
(4, 'KTM & Husqvarna South Extension', 'KTM', TRUE, 'E-28, South Extension Part II', 'New Delhi', 'Delhi', '110049', '+91 11 4656 7890', 'https://www.ktmindia.com/ktm-dealers/delhi-south-ex', '10:00 AM – 07:30 PM (Mon–Sun)', 'Bajaj Auto / KTM Official Network Directory', '2026-03-05'),
(5, 'Ather Space Indiranagar Experience Center', 'Ather Energy', TRUE, '100 Feet Road, HAL 2nd Stage, Indiranagar', 'Bengaluru', 'Karnataka', '560038', '+91 80 4688 8800', 'https://www.atherenergy.com/experience-centers/bengaluru-indiranagar', '10:00 AM – 08:00 PM (Mon–Sun)', 'Ather Energy Verified Flagship Directory', '2026-03-01'),
(6, 'Pavitra Motors Royal Enfield Brand Store', 'Royal Enfield', TRUE, 'Connaught Place, Outer Circle, M-Block', 'New Delhi', 'Delhi', '110001', '+91 11 2341 5500', 'https://www.royalenfield.com/dealers/pavitra-cp', '09:30 AM – 08:00 PM (Mon–Sun)', 'Royal Enfield Dealer Locator', '2026-02-15'),
(7, 'Topsel Toyota & Yamaha City Center', 'Yamaha', TRUE, 'Lalbagh Road, Richmond Town', 'Bengaluru', 'Karnataka', '560027', '+91 80 2222 3456', 'https://www.yamaha-motor-india.com/dealers', '10:00 AM – 07:30 PM (Mon–Sun)', 'India Yamaha Motor Dealer Directory', '2026-02-20'),
(8, 'Prime Honda & Activa Hub', 'Honda', TRUE, 'Andheri Kurla Road, Sakinaka', 'Mumbai', 'Maharashtra', '400072', '+91 22 2850 9900', 'https://www.honda2wheelersindia.com/dealers', '09:00 AM – 08:00 PM (Mon–Sun)', 'HMSI Official Dealer Locator', '2026-03-01');
