package com.example.negstudies.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CreateReservationRequest {

    @NotNull(message = "deviceId is required")
    private Long deviceId;

    @NotBlank(message = "reservedBy is required")
    @Size(max = 120, message = "reservedBy must be at most 120 characters")
    private String reservedBy;

    @NotNull(message = "startAt is required")
    private OffsetDateTime startAt;

    @NotNull(message = "endAt is required")
    private OffsetDateTime endAt;

    @Size(max = 300, message = "purpose must be at most 300 characters")
    private String purpose;
}
