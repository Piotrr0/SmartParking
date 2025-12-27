package com.smartparking.backend.repository;

import com.smartparking.backend.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {}