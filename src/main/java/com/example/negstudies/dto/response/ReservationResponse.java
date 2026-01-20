package com.example.negstudies.dto.response;

import java.time.OffsetDateTime;

public record ReservationResponse(
        Long id,
        Long deviceId,
        String deviceName,
        String reservedBy,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String purpose
) {}
