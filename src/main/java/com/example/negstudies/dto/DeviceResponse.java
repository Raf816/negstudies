package com.example.negstudies.dto;

import com.example.negstudies.model.DeviceStatus;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceResponse {
    Long id;
    String name;
    String serialNumber;
    String model;
    DeviceStatus status;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}