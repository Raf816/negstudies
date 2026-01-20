package com.example.negstudies.repository;

import com.example.negstudies.model.Reservation;
import com.example.negstudies.model.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByDeviceIdAndStatusAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
            Long deviceId,
            ReservationStatus status,
            LocalDate startDate,
            LocalDate endDate);

    List<Reservation> findAllByDeviceId(Long deviceId);
}