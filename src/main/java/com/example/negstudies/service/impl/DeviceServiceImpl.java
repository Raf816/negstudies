package com.example.negstudies.service.impl;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.response.DeviceResponse;
import com.example.negstudies.entity.Device;
import com.example.negstudies.exception.ConflictException;
import com.example.negstudies.exception.NotFoundException;
import com.example.negstudies.mapper.DeviceMapper;
import com.example.negstudies.repository.DeviceRepository;
import com.example.negstudies.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public DeviceResponse create(CreateDeviceRequest request) {
        String assetTag = request.getAssetTag().trim();
        if (deviceRepository.existsByAssetTag(assetTag)) {
            throw new ConflictException("Device with assetTag already exists: " + assetTag);
        }

        Device device = DeviceMapper.toEntity(request);
        device.setAssetTag(assetTag);

        return DeviceMapper.toResponse(deviceRepository.save(device));
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponse getById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));
        return DeviceMapper.toResponse(device);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceResponse> listAll() {
        return deviceRepository.findAll()
                .stream()
                .map(DeviceMapper::toResponse)
                .toList();
    }
}
