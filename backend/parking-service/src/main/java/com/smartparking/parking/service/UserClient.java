package com.smartparking.parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    public boolean checkUserExists(Long userId) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(userServiceUrl + "/api/users/" + userId,
                    Object.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deductWallet(Long userId, double amount) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    userServiceUrl + "/api/wallet/" + userId + "/deduct?amount=" + amount,
                    null,
                    String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
