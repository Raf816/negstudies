package com.example.negstudies.controller;

import com.example.negstudies.dto.request.CreateDeviceRequest;
import com.example.negstudies.dto.request.PatchDeviceRequest;
import com.example.negstudies.dto.request.UpdateDeviceRequest;
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

    @PutMapping("/{id}")
    public DeviceResponse update(@PathVariable Long id, @Valid @RequestBody UpdateDeviceRequest request) {
        return deviceService.update(id, request);
    }

    @PatchMapping("/{id}")
    public DeviceResponse patch(@PathVariable Long id, @RequestBody PatchDeviceRequest request) {
        // PATCH supports partial updates; field-level validation is limited without extra work.
        return deviceService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deviceService.delete(id);
    }


}
