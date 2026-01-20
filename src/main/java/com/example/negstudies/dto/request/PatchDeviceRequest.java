package com.example.negstudies.dto.request;

import com.example.negstudies.entity.DeviceStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchDeviceRequest {

    @Size(max = 120, message = "Device name must be at most 120 characters")
    private String name;

    @Size(max = 80, message = "Asset tag must be at most 80 characters")
    private String assetTag;

    private DeviceStatus status;
}
