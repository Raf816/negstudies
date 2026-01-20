package com.example.negstudies.mapper;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.response.DeviceResponse;
import com.example.negstudies.entity.Device;

public final class DeviceMapper {
    private DeviceMapper() {}

    public static Device toEntity(CreateDeviceRequest req) {
        return Device.builder()
                .name(req.getName().trim())
                .assetTag(req.getAssetTag().trim())
                .status(req.getStatus())
                .build();
    }

    public static DeviceResponse toResponse(Device d) {
        return new DeviceResponse(d.getId(), d.getName(), d.getAssetTag(), d.getStatus());
    }
}
