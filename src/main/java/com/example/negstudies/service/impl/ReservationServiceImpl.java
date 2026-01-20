package com.example.negstudies.service.impl;

import com.example.negstudies.dto.request.CreateReservationRequest;
import com.example.negstudies.dto.response.ReservationResponse;
import com.example.negstudies.entity.Device;
import com.example.negstudies.entity.DeviceStatus;
import com.example.negstudies.entity.Reservation;
import com.example.negstudies.exception.BadRequestException;
import com.example.negstudies.exception.ConflictException;
import com.example.negstudies.exception.NotFoundException;
import com.example.negstudies.mapper.ReservationMapper;
import com.example.negstudies.repository.DeviceRepository;
import com.example.negstudies.repository.ReservationRepository;
import com.example.negstudies.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        OffsetDateTime start = request.getStartAt();
        OffsetDateTime end = request.getEndAt();

        if (!start.isBefore(end)) {
            throw new BadRequestException("startAt must be before endAt");
        }

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new NotFoundException("Device not found: " + request.getDeviceId()));

        if (device.getStatus() != DeviceStatus.AVAILABLE) {
            throw new ConflictException("Device is not available for reservation (status=" + device.getStatus() + ")");
        }

        if (reservationRepository.existsOverlapping(device.getId(), start, end)) {
            throw new ConflictException("Reservation overlaps with an existing reservation for deviceId=" + device.getId());
        }

        Reservation reservation = Reservation.builder()
                .device(device)
                .reservedBy(request.getReservedBy().trim())
                .startAt(start)
                .endAt(end)
                .purpose(request.getPurpose() == null ? null : request.getPurpose().trim())
                .build();

        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));
        return ReservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> listAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }
}
