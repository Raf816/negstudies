package com.example.negstudies.dto;

import com.example.negstudies.model.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String serialNumber;

    @NotBlank
    @Size(max = 100)
    private String model;

    @NotNull
    private DeviceStatus status;
}