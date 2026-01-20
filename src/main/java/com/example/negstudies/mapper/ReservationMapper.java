package com.example.negstudies.mapper;

import com.example.negstudies.dto.response.ReservationResponse;
import com.example.negstudies.entity.Reservation;

public final class ReservationMapper {
    private ReservationMapper() {}

    public static ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getDevice().getId(),
                r.getDevice().getName(),
                r.getReservedBy(),
                r.getStartAt(),
                r.getEndAt(),
                r.getPurpose()
        );
    }
}
