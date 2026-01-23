package com.smartparking.frontend.service;

import com.smartparking.frontend.dto.TopUpRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WalletService {
    private static final String API_URL = "http://localhost:8081/api/wallet";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public double getBalance(Long userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode node = mapper.readTree(response.body());
                return node.get("balance").asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public String topUp(Long userId, double amount, String cardNum, String cardHolder, String cvv) {
        try {
            TopUpRequest dto = new TopUpRequest();
            dto.setAmount(amount);
            dto.setCardNumber(cardNum);
            dto.setCardHolder(cardHolder);
            dto.setCvv(cvv);

            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + userId + "/topup"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "Success";
            } else {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection Error";
        }
    }
}