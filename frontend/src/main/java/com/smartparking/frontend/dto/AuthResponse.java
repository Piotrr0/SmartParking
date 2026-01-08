package com.smartparking.frontend.dto;

public class AuthResponse {
    private Long id;
    private String username;
    private String message;

    public AuthResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}