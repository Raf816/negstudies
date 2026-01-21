BEGIN;

CREATE TABLE devices (
                         id          BIGSERIAL PRIMARY KEY,
                         name        VARCHAR(120) NOT NULL,
                         asset_tag   VARCHAR(80)  NOT NULL UNIQUE,
                         status      VARCHAR(30)  NOT NULL,
                         CONSTRAINT chk_devices_status
                             CHECK (status IN ('AVAILABLE', 'MAINTENANCE', 'RETIRED'))
);

CREATE TABLE reservations (
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

                              CONSTRAINT chk_reservations_time
                                  CHECK (start_at < end_at)
);

CREATE INDEX idx_reservations_device_start_end
    ON reservations(device_id, start_at, end_at);

COMMIT;
