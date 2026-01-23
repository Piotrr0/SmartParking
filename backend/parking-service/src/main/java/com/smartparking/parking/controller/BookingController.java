package com.smartparking.parking.controller;

import com.smartparking.parking.dto.BookingRequest;
import com.smartparking.parking.dto.BookingResponse;
import com.smartparking.parking.entity.Booking;
import com.smartparking.parking.entity.ParkingSpot;
import com.smartparking.parking.repository.BookingRepository;
import com.smartparking.parking.repository.ParkingSpotRepository;
import com.smartparking.parking.service.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private UserClient userClient;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            LocalDateTime startTime = request.getStartTime().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime endTime = startTime.plusHours(request.getDurationInHours());
            List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                    request.getSpotId(),
                    startTime,
                    endTime);

            if (!overlaps.isEmpty()) {
                return ResponseEntity.badRequest().body("Spot is already booked for this time period.");
            }

            ParkingSpot spot = spotRepository.findById(request.getSpotId())
                    .orElseThrow(() -> new RuntimeException("Spot not found"));

            if (!userClient.checkUserExists(request.getUserId())) {
                return ResponseEntity.badRequest().body("User not found");
            }

            double price = spot.getPricePerHour() * request.getDurationInHours();
            if ("WALLET".equalsIgnoreCase(request.getPaymentMethod())) {
                if (!userClient.deductWallet(request.getUserId(), price)) {
                    return ResponseEntity.badRequest()
                            .body("Wallet deduction failed (insufficient balance or wallet not found).");
                }
            } else {
                if (request.getCardNumber() == null || request.getCardNumber().length() != 16) {
                    return ResponseEntity.badRequest().body("Invalid Card Number");
                }
                if (request.getCvv() == null || request.getCvv().length() != 3) {
                    return ResponseEntity.badRequest().body("Invalid CVV (must be 3 digits)");
                }
                if (request.getExpiry() == null || request.getExpiry().isEmpty()) {
                    return ResponseEntity.badRequest().body("Expiration Date is required");
                }
            }

            Booking booking = new Booking(request.getUserId(), spot, startTime, endTime, price, "PAID");
            bookingRepository.save(booking);

            LocalDateTime now = LocalDateTime.now();
            if (startTime.isBefore(now.plusMinutes(5)) && endTime.isAfter(now)) {
                spot.setOccupied(true);
                spot.setExpirationTime(endTime);
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
                        b.getPaymentStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
