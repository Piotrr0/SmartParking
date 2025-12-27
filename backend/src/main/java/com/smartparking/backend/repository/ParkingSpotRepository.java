package com.smartparking.backend.repository;

import com.smartparking.backend.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {}