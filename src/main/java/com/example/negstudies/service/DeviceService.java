package com.example.negstudies.service;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.request.PatchDeviceRequest;
import com.example.negstudies.dto.request.UpdateDeviceRequest;
import com.example.negstudies.dto.response.DeviceResponse;

import java.util.List;

public interface DeviceService {
    DeviceResponse create(CreateDeviceRequest request);
    DeviceResponse getById(Long id);
    List<DeviceResponse> listAll();
    DeviceResponse update(Long id, UpdateDeviceRequest request);
    DeviceResponse patch(Long id, PatchDeviceRequest request);

}
