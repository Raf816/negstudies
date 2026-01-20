package com.example.negstudies.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "devices",
        uniqueConstraints = @UniqueConstraint(name = "uk_devices_asset_tag", columnNames = "asset_tag"),
        indexes = {
                @Index(name = "idx_devices_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "asset_tag", nullable = false, length = 80)
    private String assetTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private DeviceStatus status;
}
