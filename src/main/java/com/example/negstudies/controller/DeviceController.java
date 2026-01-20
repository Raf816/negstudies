package com.example.negstudies.controller;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.response.DeviceResponse;
import com.example.negstudies.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest request) {
        return deviceService.create(request);
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Long id) {
        return deviceService.getById(id);
    }

    @GetMapping
    public List<DeviceResponse> listAll() {
        return deviceService.listAll();
    }
}
