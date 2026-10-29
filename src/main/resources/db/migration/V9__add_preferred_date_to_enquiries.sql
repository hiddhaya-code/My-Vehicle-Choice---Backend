-- ============================================================================
-- MyVehicleChoice Database Migration: V9 Add Preferred Date to Dealer Enquiries
-- ============================================================================

ALTER TABLE dealer_enquiries
ADD COLUMN preferred_date DATE NULL AFTER enquiry_type;
