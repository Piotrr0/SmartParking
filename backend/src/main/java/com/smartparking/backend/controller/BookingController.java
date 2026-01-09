package com.smartparking.backend.controller;

import com.smartparking.backend.dto.BookingRequest;
import com.smartparking.backend.dto.BookingResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ParkingSpotRepository spotRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime endTime = startTime.plusHours(request.getDurationInHours());

            List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                    request.getSpotId(),
                    startTime,
                    endTime
            );

            if (!overlaps.isEmpty()) {
                return ResponseEntity.badRequest().body("Spot is already booked for this time period.");
            }

            ParkingSpot spot = spotRepository.findById(request.getSpotId())
                    .orElseThrow(() -> new RuntimeException("Spot not found"));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (request.getCardNumber() == null || request.getCardNumber().length() < 10) {
                return ResponseEntity.badRequest().body("Invalid Card Number");
            }

            double price = spot.getPricePerHour() * request.getDurationInHours();
            Booking booking = new Booking(user, spot, startTime, endTime, price, "PAID");
            bookingRepository.save(booking);

            LocalDateTime now = LocalDateTime.now();
            if (startTime.isBefore(now.plusMinutes(5)) && endTime.isAfter(now)) {
                spot.setOccupied(true);
                spotRepository.save(spot);
            }

            return ResponseEntity.ok("Booking confirmed! Total: $" + String.format("%.2f", price));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<Booking> bookings = bookingRepository.findBookingsForUser(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<BookingResponse> response = bookings.stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getSpot().getParkingArea() != null ? b.getSpot().getParkingArea().getName() : "Unknown Area",
                        b.getSpot().getParkingArea() != null ? b.getSpot().getParkingArea().getCity() : "Unknown City",
                        b.getSpot().getLabel(),
                        b.getStartTime().format(formatter),
                        b.getEndTime().format(formatter),
                        b.getTotalPrice(),
                        b.getPaymentStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}