package com.smartparking.backend.dto;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        String avatar
) {}