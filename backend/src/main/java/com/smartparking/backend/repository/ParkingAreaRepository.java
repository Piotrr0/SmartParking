package com.smartparking.backend.repository;

import com.smartparking.backend.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {
    Optional<ParkingArea> findByName(String name);
    List<ParkingArea> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city);
}