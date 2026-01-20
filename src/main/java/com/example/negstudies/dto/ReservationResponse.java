package com.example.negstudies.dto;

import com.example.negstudies.model.ReservationStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationResponse {
    Long id;
    Long deviceId;
    String deviceName;
    String reservedBy;
    LocalDate startDate;
    LocalDate endDate;
    String purpose;
    ReservationStatus status;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}