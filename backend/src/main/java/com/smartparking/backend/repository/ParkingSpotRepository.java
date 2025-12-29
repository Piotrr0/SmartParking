package com.smartparking.backend.repository;

import com.smartparking.backend.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByOccupiedTrueAndExpirationTimeBefore(LocalDateTime now);
}