package com.smartparking.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparking.frontend.dto.AuthRequest;
import com.smartparking.frontend.dto.AuthResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {
    private static final String API_URL = "http://localhost:8081/api/auth";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthResponse login(String username, String password) {
        try {
            AuthRequest dto = new AuthRequest(username, password, null);
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), AuthResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String username, String password, String email) {
        try {
            AuthRequest dto = new AuthRequest(username, password, email);
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}