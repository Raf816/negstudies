package com.example.negstudies.dto.response;

import com.example.negstudies.entity.DeviceStatus;

public record DeviceResponse(
        Long id,
        String name,
        String assetTag,
        DeviceStatus status
) {}
