package com.smartparking.backend.controller;

import com.smartparking.backend.dto.ParkingAreaRequest;
import com.smartparking.backend.dto.ParkingSpotRequest;
import com.smartparking.backend.entity.Booking;
import com.smartparking.backend.entity.ParkingArea;
import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.repository.BookingRepository;
import com.smartparking.backend.repository.ParkingAreaRepository;
import com.smartparking.backend.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingAreaRepository areaRepository;
    @Autowired
    private ParkingSpotRepository spotRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/areas")
    public List<ParkingArea> getAllAreas() {
        return areaRepository.findAll();
    }

    @PostMapping("/spots/add")
    public ResponseEntity<String> addSpot(@RequestBody SpotAddRequest request) {
        ParkingArea area = areaRepository.findById(request.areaId())
                .orElseThrow(() -> new RuntimeException("Area not found"));

        ParkingSpot spot = new ParkingSpot(
                request.label(),
                request.price(),
                request.type(),
                area,
                false,
                null
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

    @PostMapping("/spots/{id}/book")
    public ResponseEntity<String> bookSpot(@PathVariable Long id, @RequestParam int hours) {
        return spotRepository.findById(id)
                .map(spot -> {
                    if (spot.isOccupied()) {
                        return ResponseEntity.badRequest().body("Spot is already occupied");
                    }
                    spot.setOccupied(true);
                    spot.setExpirationTime(LocalDateTime.now().plusHours(hours));
                    spotRepository.save(spot);
                    return ResponseEntity.ok("Spot booked for " + hours + " hours");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/areas/{id}/availability")
    public ParkingAreaRequest getAreaAvailability(
            @PathVariable Long id,
            @RequestParam("start") String startStr,
            @RequestParam("duration") int duration) {

        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = start.plusHours(duration);
        ParkingArea area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        List<Booking> conflicts = bookingRepository.findOverlappingBookingsForArea(id, start, end);
        Set<Long> bookedSpotIds = conflicts.stream()
                .map(b -> b.getSpot().getId())
                .collect(Collectors.toSet());

        ParkingAreaRequest areaDTO = new ParkingAreaRequest();
        areaDTO.setId(area.getId());
        areaDTO.setName(area.getName());
        areaDTO.setCity(area.getCity());

        List<ParkingSpotRequest> spotDTOs = new ArrayList<>();

        for (ParkingSpot spot : area.getSpots()) {
            ParkingSpotRequest spotDTO = new ParkingSpotRequest();
            spotDTO.setId(spot.getId());
            spotDTO.setLabel(spot.getLabel());
            spotDTO.setType(spot.getType());
            spotDTO.setPricePerHour(spot.getPricePerHour());

            boolean reservedInFuture = bookedSpotIds.contains(spot.getId());
            spotDTO.setOccupied(reservedInFuture);
            spotDTOs.add(spotDTO);
        }

        areaDTO.setSpots(spotDTOs);
        return areaDTO;
    }

    @GetMapping("/areas/search")
    public List<ParkingAreaRequest> searchAreas(@RequestParam String query) {
        List<ParkingArea> areas = areaRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query);

        return areas.stream().map(area -> {
            ParkingAreaRequest dto = new ParkingAreaRequest();
            dto.setId(area.getId());
            dto.setName(area.getName());
            dto.setCity(area.getCity());
            return dto;
        }).collect(Collectors.toList());
    }
    public record SpotAddRequest(Long areaId, String label, double price, String type) {}
}