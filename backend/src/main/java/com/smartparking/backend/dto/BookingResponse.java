package com.smartparking.backend.dto;



public record BookingResponse(
        Long bookingId,
        String parkingName,
        String city,
        String spotLabel,
        String startTime,
        String endTime,
        double totalPrice,
        String status
) {}