package com.example.negstudies.service;

import com.example.negstudies.dto.ReservationRequest;
import com.example.negstudies.dto.ReservationResponse;
import com.example.negstudies.exception.BadRequestException;
import com.example.negstudies.exception.ConflictException;
import com.example.negstudies.exception.NotFoundException;
import com.example.negstudies.model.Device;
import com.example.negstudies.model.DeviceStatus;
import com.example.negstudies.model.Reservation;
import com.example.negstudies.model.ReservationStatus;
import com.example.negstudies.repository.DeviceRepository;
import com.example.negstudies.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DeviceRepository deviceRepository;

    public ReservationResponse create(Long deviceId, ReservationRequest request) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("Device not found."));
        if (device.getStatus() != DeviceStatus.AVAILABLE) {
            throw new BadRequestException("Device is not available for reservation.");
        }
        validateDates(request.getStartDate(), request.getEndDate());
        boolean overlaps = reservationRepository.existsByDeviceIdAndStatusAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
                deviceId,
                ReservationStatus.APPROVED,
                request.getStartDate(),
                request.getEndDate());
        if (overlaps) {
            throw new ConflictException("Device is already reserved for the selected dates.");
        }
        Reservation reservation = new Reservation();
        reservation.setDevice(device);
        reservation.setReservedBy(request.getReservedBy());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservation.setPurpose(request.getPurpose());
        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> list(Long deviceId) {
        List<Reservation> reservations = deviceId == null
                ? reservationRepository.findAll()
                : reservationRepository.findAllByDeviceId(deviceId);
        return reservations.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationResponse get(Long id) {
        return toResponse(findReservation(id));
    }

    public ReservationResponse cancel(Long id) {
        Reservation reservation = findReservation(id);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("Reservation is already cancelled.");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.touch();
        return toResponse(reservation);
    }

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found."));
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("End date must be on or after start date.");
        }
    }

    private ReservationResponse toResponse(Reservation reservation) {
        Device device = reservation.getDevice();
        return ReservationResponse.builder()
                .id(reservation.getId())
                .deviceId(device.getId())
                .deviceName(device.getName())
                .reservedBy(reservation.getReservedBy())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .purpose(reservation.getPurpose())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}