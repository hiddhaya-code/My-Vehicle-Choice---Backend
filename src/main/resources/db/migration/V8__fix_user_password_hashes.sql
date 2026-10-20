-- ============================================================================
-- MyVehicleChoice Database Migration: V8 Fix User BCrypt Password Hashes
-- ============================================================================

-- Ensure Admin and Customer demo accounts have verified BCrypt hashes
-- Credentials:
-- Admin: admin@myvehiclechoice.in / Admin@123 (ROLE_ADMIN)
-- Customer: rahul@example.com / Customer@123 (ROLE_USER)

UPDATE users 
SET password_hash = '$2a$10$pyJ2M/8TEK/hSTbItj3WwuaIzxl93h4ObGkO0w8lDXSwbhlxFi9du' 
WHERE email = 'admin@myvehiclechoice.in';

UPDATE users 
SET password_hash = '$2a$10$5y78TSiHJWPKrVB0.i0.a.JvZdBJI3PEwDjQq70OXXSD4Fu25fb9S' 
WHERE email = 'rahul@example.com';
