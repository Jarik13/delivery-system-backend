package org.deliverysystem.com.dtos.users;

public record CreateUserDto(
        String email,
        String firstName,
        String lastName,
        String role
) {}