package com.smartparking.backend.repository;

import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByOccupiedTrueAndExpirationTimeBefore(LocalDateTime now);
    Optional<ParkingSpot> findByLabelAndParkingArea(String label, ParkingArea parkingArea);
}