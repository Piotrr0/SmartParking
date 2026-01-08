package com.smartparking.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparking.frontend.dto.BookingRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingService {
    private static final String API_URL = "http://localhost:8080/api/bookings";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String createBooking(Long userId, Long spotId, LocalDateTime start, int hours, String cardNum, String name) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = start.format(formatter);

            BookingRequest bookingRequest = new BookingRequest(userId, spotId, formattedDate, hours, cardNum, name);

            String jsonBody = mapper.writeValueAsString(bookingRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "Error (" + response.statusCode() + "): " + response.body();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Connection Error: " + e.getMessage();
        }
    }
}