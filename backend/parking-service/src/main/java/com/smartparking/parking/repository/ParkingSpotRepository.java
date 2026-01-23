package com.smartparking.parking.repository;

import com.smartparking.parking.entity.ParkingSpot;
import com.smartparking.parking.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByOccupiedTrueAndExpirationTimeBefore(LocalDateTime now);

    Optional<ParkingSpot> findByLabelAndParkingArea(String label, ParkingArea parkingArea);
}
