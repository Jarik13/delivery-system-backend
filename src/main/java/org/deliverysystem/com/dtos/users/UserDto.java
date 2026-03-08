package org.deliverysystem.com.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Дані користувача системи")
public record UserDto(
        @Schema(description = "Keycloak ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String keycloakId,

        @Schema(description = "Email", example = "ivan.petrenko@gmail.com")
        String email,

        @Schema(description = "Ім'я", example = "Іван")
        String firstName,

        @Schema(description = "Прізвище", example = "Петренко")
        String lastName,

        @Schema(description = "По батькові", example = "Миколайович")
        String middleName,

        @Schema(description = "Номер телефону", example = "+380991234567")
        String phoneNumber,

        @Schema(description = "Роль", example = "EMPLOYEE")
        String role,

        @Schema(description = "Email підтверджено", example = "false")
        boolean emailVerified,

        @Schema(description = "ID відділення (тільки для EMPLOYEE)", example = "236")
        Integer branchId
) {}