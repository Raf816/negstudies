cat > init.sql <<'SQL'
-- =====================================================
-- DEVICE STATUS ENUM
-- =====================================================
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'device_status') THEN
CREATE TYPE device_status AS ENUM ('AVAILABLE','RESERVED','MAINTENANCE');
END IF;
END$$;

-- =====================================================
-- DEVICES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS devices (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    asset_tag VARCHAR(100) NOT NULL UNIQUE,
    status device_status NOT NULL DEFAULT 'AVAILABLE'
    );

CREATE INDEX IF NOT EXISTS idx_devices_status ON devices(status);

-- =====================================================
-- RESERVATIONS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS reservations (
                                            id BIGSERIAL PRIMARY KEY,
                                            device_id BIGINT NOT NULL,
                                            reserved_by VARCHAR(255) NOT NULL,
    purpose TEXT,
    start_at TIMESTAMPTZ NOT NULL,
    end_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_reservation_device
    FOREIGN KEY (device_id)
    REFERENCES devices(id)
    ON DELETE CASCADE,

    CONSTRAINT chk_reservation_dates
    CHECK (end_at > start_at)
    );

CREATE INDEX IF NOT EXISTS idx_reservations_device_time
    ON reservations(device_id, start_at, end_at);

-- =====================================================
-- SEED DATA: DEVICES
-- =====================================================
INSERT INTO devices (name, asset_tag, status)
VALUES
    ('iPhone 15 Pro', 'IPH-001', 'AVAILABLE'),
    ('iPhone 15 Pro Max', 'IPH-002', 'AVAILABLE'),
    ('iPhone 14', 'IPH-003', 'RESERVED'),
    ('Samsung Galaxy S23', 'SAM-001', 'AVAILABLE'),
    ('Samsung Galaxy S22', 'SAM-002', 'MAINTENANCE'),
    ('Google Pixel 8', 'PIX-001', 'AVAILABLE'),
    ('iPad Pro 12.9', 'IPAD-001', 'AVAILABLE'),
    ('iPad Mini', 'IPAD-002', 'AVAILABLE'),
    ('MacBook Pro 14', 'MAC-001', 'MAINTENANCE'),
    ('MacBook Air M2', 'MAC-002', 'AVAILABLE')
    ON CONFLICT (asset_tag) DO NOTHING;

-- =====================================================
-- SEED DATA: RESERVATIONS (NO OVERLAPS)
-- =====================================================
INSERT INTO reservations (device_id, reserved_by, purpose, start_at, end_at)
SELECT
    d.id,
    r.reserved_by,
    r.purpose,
    r.start_at,
    r.end_at
FROM (
         VALUES
             ('IPH-003', 'Raf Ahmed', 'Mobile app testing',
              '2026-01-21 09:00+00'::timestamptz, '2026-01-21 12:00+00'::timestamptz),

             ('IPH-001', 'Jane Smith', 'Client demo',
              '2026-01-22 10:00+00'::timestamptz, '2026-01-22 13:00+00'::timestamptz),

             ('SAM-001', 'John Doe', 'Android compatibility testing',
              '2026-01-23 08:30+00'::timestamptz, '2026-01-23 11:30+00'::timestamptz),

             ('IPAD-001', 'QA Team', 'Tablet UI regression testing',
              '2026-01-24 09:00+00'::timestamptz, '2026-01-24 17:00+00'::timestamptz),

             ('PIX-001', 'Security Team', 'authentication testing',
              '2026-01-25 10:00+00'::timestamptz, '2026-01-25 15:00+00'::timestamptz)
     ) AS r(asset_tag, reserved_by, purpose, start_at, end_at)
         JOIN devices d ON d.asset_tag = r.asset_tag
WHERE NOT EXISTS (
    SELECT 1
    FROM reservations existing
    WHERE existing.device_id = d.id
      AND existing.start_at < r.end_at
      AND existing.end_at > r.start_at
);
SQL
