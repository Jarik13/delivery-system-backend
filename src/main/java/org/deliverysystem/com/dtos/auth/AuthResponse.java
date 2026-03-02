package org.deliverysystem.com.dtos.auth;

public record AuthResponse(
        String accessToken,
        String email,
        String role
) {}