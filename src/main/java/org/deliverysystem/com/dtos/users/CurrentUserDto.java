package org.deliverysystem.com.dtos.users;

public record CurrentUserDto(Integer id, String keycloakId, String role) {}