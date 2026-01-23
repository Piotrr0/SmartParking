package com.smartparking.parking.repository;

import com.smartparking.parking.entity.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {
    Optional<ParkingArea> findByName(String name);

    List<ParkingArea> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city);
}
