package com.example.negstudies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "reservations",
        indexes = {
                @Index(name = "idx_reservations_device_start_end", columnList = "device_id,start_at,end_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many reservations can belong to one device
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "device_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservations_device")
    )
    private Device device;

    @Column(name = "reserved_by", nullable = false, length = 120)
    private String reservedBy;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @Column(name = "purpose", length = 300)
    private String purpose;
}
