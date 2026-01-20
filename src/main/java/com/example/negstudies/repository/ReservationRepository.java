package com.example.negstudies.repository;

import com.example.negstudies.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        select count(r) > 0
        from Reservation r
        where r.device.id = :deviceId
          and r.startAt < :endAt
          and r.endAt > :startAt
    """)
    boolean existsOverlapping(Long deviceId, OffsetDateTime startAt, OffsetDateTime endAt);
}
