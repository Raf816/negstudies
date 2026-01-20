package com.example.negstudies.service;

import com.example.negstudies.dto.DeviceRequest;
import com.example.negstudies.dto.DeviceResponse;
import com.example.negstudies.exception.BadRequestException;
import com.example.negstudies.exception.NotFoundException;
import com.example.negstudies.model.Device;
import com.example.negstudies.repository.DeviceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceResponse create(DeviceRequest request) {
        deviceRepository.findBySerialNumber(request.getSerialNumber())
                .ifPresent(device -> {
                    throw new BadRequestException("Device serial number already exists.");
                });
        Device device = new Device();
        applyRequest(device, request);
        Device saved = deviceRepository.save(device);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponse> list() {
        return deviceRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeviceResponse get(Long id) {
        return toResponse(findDevice(id));
    }

    public DeviceResponse update(Long id, DeviceRequest request) {
        Device device = findDevice(id);
        if (!device.getSerialNumber().equalsIgnoreCase(request.getSerialNumber())) {
            deviceRepository.findBySerialNumber(request.getSerialNumber())
                    .ifPresent(existing -> {
                        throw new BadRequestException("Device serial number already exists.");
                    });
        }
        applyRequest(device, request);
        device.touch();
        return toResponse(device);
    }

    public void delete(Long id) {
        Device device = findDevice(id);
        deviceRepository.delete(device);
    }

    private Device findDevice(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found."));
    }

    private void applyRequest(Device device, DeviceRequest request) {
        device.setName(request.getName());
        device.setSerialNumber(request.getSerialNumber());
        device.setModel(request.getModel());
        device.setStatus(request.getStatus());
    }

    private DeviceResponse toResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .name(device.getName())
                .serialNumber(device.getSerialNumber())
                .model(device.getModel())
                .status(device.getStatus())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }
}