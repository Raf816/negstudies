package com.example.negstudies.repository;

import com.example.negstudies.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByAssetTag(String assetTag);

    boolean existsByAssetTag(String assetTag);
}
