package org.deliverysystem.com.dtos.clients;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Створення нового клієнта")
public record CreateClientDto(
        @NotBlank(message = "Ім'я обов'язкове")
        @Schema(description = "Ім'я", example = "Іван")
        String firstName,

        @NotBlank(message = "Прізвище обов'язкове")
        @Schema(description = "Прізвище", example = "Петренко")
        String lastName,

        @Schema(description = "По-батькові", example = "Олегович")
        String middleName,

        @NotBlank(message = "Телефон обов'язковий")
        @Pattern(regexp = "^\\+380\\d{9}$", message = "Формат телефону: +380xxxxxxxxx")
        @Schema(description = "Телефон", example = "+380501234567")
        String phoneNumber,

        @Email(message = "Невірний формат email")
        @Schema(description = "Email", example = "ivan@mail.com")
        String email
) {}