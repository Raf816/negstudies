-- db/seed.sql
-- Seed data for Device Reservation application

BEGIN;

-- Devices
INSERT INTO devices (name, asset_tag, status)
VALUES
    ('iPhone 15 Pro', 'IPH-001', 'AVAILABLE'),
    ('Samsung Galaxy S24', 'SAM-001', 'AVAILABLE'),
    ('iPhone 16"', 'IPD-001', 'AVAILABLE'),
    ('iPhone 16"', 'IPH-002', 'AVAILABLE'),
    ('iPhone 17"', 'IPD-003', 'AVAILABLE'),
    ('Samsung Galaxy S25', 'SAM-002', 'MAINTENANCE'),

ON CONFLICT (asset_tag) DO NOTHING;

-- Reservations
INSERT INTO reservations (device_id, reserved_by, start_at, end_at, purpose)
SELECT d.id,
       'Seed User',
       '2026-01-22T09:00:00Z',
       '2026-01-22T12:00:00Z',
       'Initial seeded reservation'
FROM devices d
WHERE d.asset_tag = 'IPH-001'
    ON CONFLICT DO NOTHING;

COMMIT;
