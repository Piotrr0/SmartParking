package com.smartparking.backend.controller;

import com.smartparking.backend.entity.ParkingArea;
import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.repository.ParkingAreaRepository;
import com.smartparking.backend.repository.ParkingSpotRepository;
import com.smartparking.backend.dto.ParkingAreaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingAreaRepository areaRepository;
    @Autowired
    private ParkingSpotRepository spotRepository;

    @GetMapping("/areas")
    public List<ParkingArea> getAllAreas() {
        return areaRepository.findAll();
    }

    @PostMapping("/spots/add")
    public ResponseEntity<String> addSpot(@RequestBody ParkingSpotRequest request) {
        ParkingArea area = areaRepository.findById(request.areaId())
                .orElseThrow(() -> new RuntimeException("Area not found"));

        ParkingSpot spot = new ParkingSpot(
                request.label(),
                request.price(),
                request.type(),
                area
        );
        spotRepository.save(spot);
        return ResponseEntity.ok("Spot added successfully");
    }

    @PostMapping("/areas/add")
    public ResponseEntity<String> addParkingArea(@RequestBody ParkingAreaRequest request) {
        ParkingArea area = new ParkingArea(request.getName(), request.getCity());

        areaRepository.save(area);
        return ResponseEntity.ok("Parking Area Created Successfully");
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<ParkingArea> getAreaById(@PathVariable Long id) {
        return areaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record ParkingSpotRequest(Long areaId, String label, double price, String type) {}
}