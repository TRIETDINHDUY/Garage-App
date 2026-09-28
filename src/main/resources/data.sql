-- ============================================================
-- SERVICE catalog
-- ============================================================
INSERT INTO service (service_code, service_name, description) VALUES
('BODY_REPAIR',      'Body Repair',          'Repair of body panels, bumpers, and structural components'),
('PAINT',            'Paint & Refinishing',  'Full and partial paint jobs, color matching'),
('MECHANICAL',       'Mechanical Repair',    'Engine, transmission, brake, and suspension work'),
('GLASS',            'Glass Replacement',    'Windshield, side window, and rear glass replacement'),
('ELECTRICAL',       'Electrical Repair',    'Wiring, sensors, ECU, and electronic system repair'),
('FULL_RESTORATION', 'Full Restoration',     'Complete vehicle restoration from frame to finish');

-- ============================================================
-- GARAGE (id auto: 1..6)
-- ============================================================
INSERT INTO garage (garage_code, garage_name, phone_number, email, status, rating, updated_date) VALUES
('GR-LA-001',  'AutoFix Los Angeles',    '213-555-0101', 'la@autofix.com',      'ACTIVE', 4.5, '2026-09-01'),
('GR-LA-002',  'QuickRepair Hollywood',  '323-555-0202', 'hwd@quickrepair.com', 'ACTIVE', 4.2, '2026-09-05'),
('GR-SF-001',  'Bay Area Body Shop',     '415-555-0303', 'sf@bayarea.com',      'ACTIVE', 4.7, '2026-08-20'),
('GR-TX-001',  'Dallas Premier Garage',  '214-555-0404', 'dallas@premier.com',  'ACTIVE', 4.0, '2026-09-10'),
('GR-NY-001',  'NYC Auto Center',        '212-555-0505', 'nyc@autocenter.com',  'ACTIVE', 3.9, '2026-09-12'),
('GR-IL-001',  'Chicago CarCare',        '312-555-0606', 'chi@carcare.com',     'INACTIVE', 3.5, '2026-07-01');

-- ============================================================
-- GARAGE_ADDRESS (garage_id → garage.garage_id)
-- ============================================================
INSERT INTO garage_address (street, city, state, postal_code, country, garage_id) VALUES
('123 Main St',       'Los Angeles',   'CA', '90001', 'USA', 1),
('456 Hollywood Blvd','Los Angeles',   'CA', '90028', 'USA', 2),
('789 Market St',     'San Francisco', 'CA', '94105', 'USA', 3),
('321 Commerce St',   'Dallas',        'TX', '75201', 'USA', 4),
('88 5th Ave',        'New York',      'NY', '10011', 'USA', 5),
('200 Michigan Ave',  'Chicago',       'IL', '60601', 'USA', 6);

-- ============================================================
-- GARAGE_SERVICE (links garage to service)
-- GR-LA-001 (1): BODY_REPAIR(1), PAINT(2), GLASS(4)
-- GR-LA-002 (2): BODY_REPAIR(1), MECHANICAL(3), ELECTRICAL(5)
-- GR-SF-001 (3): BODY_REPAIR(1), PAINT(2), FULL_RESTORATION(6)
-- GR-TX-001 (4): MECHANICAL(3), ELECTRICAL(5)
-- GR-NY-001 (5): BODY_REPAIR(1), GLASS(4), PAINT(2)
-- GR-IL-001 (6): MECHANICAL(3)
-- ============================================================
INSERT INTO garage_service (garage_id, service_id, status) VALUES
(1, 1, 'AVAILABLE'),   -- GR-LA-001: BODY_REPAIR
(1, 2, 'AVAILABLE'),   -- GR-LA-001: PAINT
(1, 4, 'AVAILABLE'),   -- GR-LA-001: GLASS
(2, 1, 'AVAILABLE'),   -- GR-LA-002: BODY_REPAIR
(2, 3, 'AVAILABLE'),   -- GR-LA-002: MECHANICAL
(2, 5, 'AVAILABLE'),   -- GR-LA-002: ELECTRICAL
(3, 1, 'AVAILABLE'),   -- GR-SF-001: BODY_REPAIR
(3, 2, 'AVAILABLE'),   -- GR-SF-001: PAINT
(3, 6, 'AVAILABLE'),   -- GR-SF-001: FULL_RESTORATION
(4, 3, 'AVAILABLE'),   -- GR-TX-001: MECHANICAL
(4, 5, 'AVAILABLE'),   -- GR-TX-001: ELECTRICAL
(5, 1, 'AVAILABLE'),   -- GR-NY-001: BODY_REPAIR
(5, 4, 'AVAILABLE'),   -- GR-NY-001: GLASS
(5, 2, 'AVAILABLE'),   -- GR-NY-001: PAINT
(6, 3, 'UNAVAILABLE'); -- GR-IL-001: MECHANICAL (inactive)

-- ============================================================
-- CUSTOMER (id auto: 1..4)
-- ============================================================
INSERT INTO customer (customer_name, phone_number, email) VALUES
('Nguyen Van A', '0901234567', 'nguyenvana@email.com'),
('Tran Thi B',   '0912345678', 'tranthib@email.com'),
('Le Van C',     '0923456789', 'levanc@email.com'),
('Pham Thi D',   '0934567890', 'phamthid@email.com');

-- ============================================================
-- VEHICLE (vehicle_id auto: 1..5; owner FK → customer.customer_id)
-- ============================================================
INSERT INTO vehicle (vehicle_vin, vehicle_make, vehicle_model, vehicle_year, owner) VALUES
('1HGBH41JXMN109186', 'Honda',   'Accord',  2022, 1),
('2T1BURHE0JC054181', 'Toyota',  'Corolla', 2021, 2),
('3VWSE69M87M150217', 'VW',      'Jetta',   2020, 3),
('1N4AL3AP4JN512211', 'Nissan',  'Altima',  2023, 4),
('5YJSA1DN5DFP14705', 'Tesla',   'Model S', 2024, 1);

-- ============================================================
-- REPAIR_ORDER
-- vehicle_id FK → vehicle; garage_service_id FK → garage_service
-- gs id 1 = GR-LA-001/BODY_REPAIR, id 4 = GR-LA-002/BODY_REPAIR, id 10 = GR-TX-001/MECHANICAL
-- ============================================================
INSERT INTO repair_order (claim_number, claim_id, loss_street, loss_city, loss_state, status, completed_date, created_date, updated_date, vehicle_id, garage_service_id) VALUES
('CLM-2026-001', 'CL-001', '100 Sunset Blvd', 'Los Angeles', 'CA', 'IN_PROGRESS', '2026-10-15', '2026-09-15T09:00:00', '2026-09-18T10:00:00', 1, 1),
('CLM-2026-002', 'CL-002', '200 Vine St',     'Los Angeles', 'CA', 'PENDING',     '2026-10-20', '2026-09-16T10:00:00', '2026-09-16T10:00:00', 2, 4),
('CLM-2026-003', 'CL-003', '300 Oak Ave',     'Dallas',      'TX', 'COMPLETED',   '2026-09-30', '2026-09-01T08:00:00', '2026-09-30T17:00:00', 3, 10),
('CLM-2026-004', 'CL-004', '400 Elm St',      'Los Angeles', 'CA', 'PENDING',     '2026-11-01', '2026-09-18T11:00:00', '2026-09-18T11:00:00', 4, 2),
('CLM-2026-005', 'CL-005', '500 Pine Rd',     'Los Angeles', 'CA', 'IN_PROGRESS', '2026-10-25', '2026-09-12T08:30:00', '2026-09-15T09:00:00', 5, 4);

-- ============================================================
-- BILLING (repair_order_id FK → repair_order)
-- ============================================================
INSERT INTO billing (labor_amount, parts_amount, tax_amount, total_amount, status, created_date, repair_order_id) VALUES
(1200.00, 800.00, 160.00, 2160.00, 'PENDING', '2026-09-16T10:00:00', 1),
(0.00,    0.00,   0.00,   0.00,    'PENDING', '2026-09-16T11:00:00', 2),
(500.00,  200.00, 56.00,  756.00,  'PAID',    '2026-09-02T10:00:00', 3);

-- ============================================================
-- REPAIR_HISTORY (repair_order_id FK → repair_order)
-- ============================================================
INSERT INTO repair_history (old_status, new_status, changed_date, changed_by, repair_order_id) VALUES
(null,         'PENDING',     '2026-09-15T09:00:00', 'ClaimCenter', 1),
('PENDING',    'IN_PROGRESS', '2026-09-18T10:00:00', 'Garage',      1),
(null,         'PENDING',     '2026-09-16T10:00:00', 'ClaimCenter', 2),
(null,         'PENDING',     '2026-09-01T08:00:00', 'ClaimCenter', 3),
('PENDING',    'IN_PROGRESS', '2026-09-10T08:00:00', 'Garage',      3),
('IN_PROGRESS','COMPLETED',   '2026-09-30T17:00:00', 'Garage',      3),
(null,         'PENDING',     '2026-09-18T11:00:00', 'ClaimCenter', 4),
(null,         'PENDING',     '2026-09-12T08:30:00', 'ClaimCenter', 5),
('PENDING',    'IN_PROGRESS', '2026-09-15T09:00:00', 'Garage',      5);
