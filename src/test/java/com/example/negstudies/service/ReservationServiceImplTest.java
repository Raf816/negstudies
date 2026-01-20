package com.example.negstudies.service;

import com.example.negstudies.dto.request.CreateReservationRequest;
import com.example.negstudies.entity.Device;
import com.example.negstudies.entity.DeviceStatus;
import com.example.negstudies.exception.ConflictException;
import com.example.negstudies.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationServiceImplTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void createReservation_whenOverlapping_shouldThrowConflict() {
        // Arrange: create a device
        Device device = deviceRepository.save(
                Device.builder()
                        .name("iPhone 15 Pro")
                        .assetTag("IPH-001")
                        .status(DeviceStatus.AVAILABLE)
                        .build()
        );

        // Arrange: create first reservation
        CreateReservationRequest first = new CreateReservationRequest();
        first.setDeviceId(device.getId());
        first.setReservedBy("Raf Ahmed");
        first.setStartAt(OffsetDateTime.parse("2026-01-21T09:00:00Z"));
        first.setEndAt(OffsetDateTime.parse("2026-01-21T12:00:00Z"));
        first.setPurpose("Testing");
        reservationService.create(first);

        // Act + Assert: second reservation overlaps -> ConflictException
        CreateReservationRequest second = new CreateReservationRequest();
        second.setDeviceId(device.getId());
        second.setReservedBy("Raf Ahmed");
        second.setStartAt(OffsetDateTime.parse("2026-01-21T10:00:00Z")); // overlaps
        second.setEndAt(OffsetDateTime.parse("2026-01-21T11:00:00Z"));
        second.setPurpose("Testing");

        ConflictException ex = assertThrows(ConflictException.class, () -> reservationService.create(second));
        assertTrue(ex.getMessage().toLowerCase().contains("overlaps"));
    }

    @Test
    void createReservation_whenTouchingEdges_shouldNotOverlap() {
        // Arrange
        Device device = deviceRepository.save(
                Device.builder()
                        .name("iPhone 15 Pro")
                        .assetTag("IPH-EDGE")
                        .status(DeviceStatus.AVAILABLE)
                        .build()
        );

        // First reservation: 09:00 -> 12:00
        CreateReservationRequest first = new CreateReservationRequest();
        first.setDeviceId(device.getId());
        first.setReservedBy("Raf Ahmed");
        first.setStartAt(OffsetDateTime.parse("2026-01-21T09:00:00Z"));
        first.setEndAt(OffsetDateTime.parse("2026-01-21T12:00:00Z"));
        reservationService.create(first);

        // Second reservation starts exactly when first ends: 12:00 -> 13:00 (allowed)
        CreateReservationRequest second = new CreateReservationRequest();
        second.setDeviceId(device.getId());
        second.setReservedBy("Raf Ahmed");
        second.setStartAt(OffsetDateTime.parse("2026-01-21T12:00:00Z"));
        second.setEndAt(OffsetDateTime.parse("2026-01-21T13:00:00Z"));

        assertDoesNotThrow(() -> reservationService.create(second));
    }
}
