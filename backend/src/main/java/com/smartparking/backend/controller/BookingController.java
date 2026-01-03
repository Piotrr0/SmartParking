package com.smartparking.backend.controller;

import com.smartparking.backend.dto.BookingRequest;
import com.smartparking.backend.entity.Booking;
import com.smartparking.backend.entity.ParkingSpot;
import com.smartparking.backend.entity.User;
import com.smartparking.backend.repository.BookingRepository;
import com.smartparking.backend.repository.ParkingSpotRepository;
import com.smartparking.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ParkingSpotRepository spotRepository;
    @Autowired private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        LocalDateTime endTime = request.getStartTime().plusHours(request.getDurationInHours());
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                request.getSpotId(),
                request.getStartTime(),
                endTime
        );

        if (!overlaps.isEmpty()) {
            return ResponseEntity.badRequest().body("Spot is already booked for this time period.");
        }

        ParkingSpot spot = spotRepository.findById(request.getSpotId()).orElseThrow(() -> new RuntimeException("Spot not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getCardNumber() == null || request.getCardNumber().length() < 16) {
            return ResponseEntity.badRequest().body("Invalid Card Number");
        }
        double price = spot.getPricePerHour() * request.getDurationInHours();
        Booking booking = new Booking(
                user,
                spot,
                request.getStartTime(),
                endTime,
                price,
                "PAID"
        );
        bookingRepository.save(booking);
        if (request.getStartTime().isBefore(LocalDateTime.now().plusMinutes(5))) {
            spot.setOccupied(true);
            spotRepository.save(spot);
        }

        return ResponseEntity.ok("Booking confirmed! Total: " + price);
    }
}