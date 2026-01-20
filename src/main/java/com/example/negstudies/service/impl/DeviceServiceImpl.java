package com.example.negstudies.service.impl;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.request.PatchDeviceRequest;
import com.example.negstudies.dto.request.UpdateDeviceRequest;
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

    @Override
    @Transactional
    public DeviceResponse update(Long id, UpdateDeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));

        String newAssetTag = request.getAssetTag().trim();

        if (deviceRepository.existsByAssetTagAndIdNot(newAssetTag, id)) {
            throw new ConflictException("Device with assetTag already exists: " + newAssetTag);
        }

        device.setName(request.getName().trim());
        device.setAssetTag(newAssetTag);
        device.setStatus(request.getStatus());

        return DeviceMapper.toResponse(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public DeviceResponse patch(Long id, PatchDeviceRequest request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));

        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isBlank()) {
                throw new com.example.negstudies.exception.BadRequestException("name must not be blank");
            }
            device.setName(name);
        }

        if (request.getAssetTag() != null) {
            String assetTag = request.getAssetTag().trim();
            if (assetTag.isBlank()) {
                throw new com.example.negstudies.exception.BadRequestException("assetTag must not be blank");
            }
            if (deviceRepository.existsByAssetTagAndIdNot(assetTag, id)) {
                throw new ConflictException("Device with assetTag already exists: " + assetTag);
            }
            device.setAssetTag(assetTag);
        }

        if (request.getStatus() != null) {
            device.setStatus(request.getStatus());
        }

        return DeviceMapper.toResponse(deviceRepository.save(device));
    }

}
