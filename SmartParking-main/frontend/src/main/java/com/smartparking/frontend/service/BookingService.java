package com.smartparking.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparking.frontend.dto.BookingRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import com.fasterxml.jackson.core.type.TypeReference;
import com.smartparking.frontend.dto.BookingResponse;
import java.util.Collections;
import java.util.List;

public class BookingService {
    private static final String API_URL = "http://localhost:8080/api/bookings";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String createBooking(Long userId, Long spotId, LocalDateTime start, int hours, String cardNum, String name) {
        try {
            BookingRequest bookingRequest = new BookingRequest(userId, spotId, start.toString(), hours, cardNum, name);
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
                return "Error: " + response.body();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Connection Error: " + e.getMessage();
        }
    }
    public List<BookingResponse> getUserBookings(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/user/" + userId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200) {
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return mapper.readValue(response.body(), new TypeReference<List<BookingResponse>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}