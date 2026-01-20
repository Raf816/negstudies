package com.example.negstudies.service;

import com.example.negstudies.dto.request.CreateReservationRequest;
import com.example.negstudies.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse create(CreateReservationRequest request);
    ReservationResponse getById(Long id);
    List<ReservationResponse> listAll();
}
