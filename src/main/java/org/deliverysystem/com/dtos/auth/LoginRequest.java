package org.deliverysystem.com.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email не може бути порожнім")
        @Email(message = "Невірний формат email")
        String email,

        @NotBlank(message = "Пароль не може бути порожнім")
        @Size(min = 6, message = "Пароль має містити мінімум 6 символів")
        String password
) {}