package org.deliverysystem.com.dtos.users;

public record UserDto(
        String keycloakId,
        String email,
        String firstName,
        String lastName,
        String role,
        boolean emailVerified
) {}