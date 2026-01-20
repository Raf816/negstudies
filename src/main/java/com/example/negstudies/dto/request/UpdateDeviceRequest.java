package com.example.negstudies.dto.request;

import com.example.negstudies.entity.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDeviceRequest {

    @NotBlank(message = "Device name is required")
    @Size(max = 120, message = "Device name must be at most 120 characters")
    private String name;

    @NotBlank(message = "Asset tag is required")
    @Size(max = 80, message = "Asset tag must be at most 80 characters")
    private String assetTag;

    @NotNull(message = "Device status is required")
    private DeviceStatus status;
}
