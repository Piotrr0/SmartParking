package com.smartparking.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparking.frontend.dto.UserProfileResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class UserService {

    private static final String API_URL = "http://localhost:8080/api/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public UserProfileResponse getUserProfile(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), UserProfileResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changePassword(Long userId, String newPassword) {
        try {
            String json = mapper.writeValueAsString(Map.of("newPassword", newPassword));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId + "/password"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAvatar(Long userId, String base64Image) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId + "/avatar"))
                    .header("Content-Type", "text/plain")
                    .PUT(HttpRequest.BodyPublishers.ofString(base64Image))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId))
                    .DELETE()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}