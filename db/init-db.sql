-- db/init-db.sql
-- Initial schema for Device Reservation application (PostgreSQL)

BEGIN;

-- Devices
CREATE TABLE IF NOT EXISTS devices (
                                       id          BIGSERIAL PRIMARY KEY,
                                       name        VARCHAR(120) NOT NULL,
    asset_tag   VARCHAR(80)  NOT NULL,
    status      VARCHAR(30)  NOT NULL,
    CONSTRAINT uk_devices_asset_tag UNIQUE (asset_tag),
    CONSTRAINT chk_devices_status CHECK (status IN ('AVAILABLE', 'MAINTENANCE', 'RETIRED'))
    );

CREATE INDEX IF NOT EXISTS idx_devices_status ON devices(status);

-- Reservations
CREATE TABLE IF NOT EXISTS reservations (
                                            id           BIGSERIAL PRIMARY KEY,
                                            device_id    BIGINT NOT NULL,
                                            reserved_by  VARCHAR(120) NOT NULL,
    start_at     TIMESTAMPTZ NOT NULL,
    end_at       TIMESTAMPTZ NOT NULL,
    purpose      VARCHAR(300),

    CONSTRAINT fk_reservations_device
    FOREIGN KEY (device_id)
    REFERENCES devices(id)
    ON DELETE RESTRICT,

    CONSTRAINT chk_reservations_time CHECK (start_at < end_at)
    );

CREATE INDEX IF NOT EXISTS idx_reservations_device_start_end
    ON reservations(device_id, start_at, end_at);

COMMIT;
