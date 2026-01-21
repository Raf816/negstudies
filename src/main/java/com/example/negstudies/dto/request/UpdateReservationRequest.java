package com.example.negstudies.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UpdateReservationRequest {

    @NotNull(message = "startAt is required")
    private OffsetDateTime startAt;

    @NotNull(message = "endAt is required")
    private OffsetDateTime endAt;

    @Size(max = 300, message = "purpose must be at most 300 characters")
    private String purpose;
}
