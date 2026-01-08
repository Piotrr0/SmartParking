package com.smartparking.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.smartparking.frontend.dto.ParkingAreaRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ParkingService {
    private static final String API_URL = "http://localhost:8080/api/parking";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<ParkingAreaRequest> getAllParkingAreas() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/areas"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<ParkingAreaRequest>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public ParkingAreaRequest getParkingAreaById(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/areas/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), ParkingAreaRequest.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ParkingAreaRequest getParkingAreaAvailability(Long areaId, String startDateTime, int durationHours) {
        try {
            String url = String.format("%s/areas/%d/availability?start=%s&duration=%d",
                    API_URL, areaId, startDateTime, durationHours);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), ParkingAreaRequest.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}