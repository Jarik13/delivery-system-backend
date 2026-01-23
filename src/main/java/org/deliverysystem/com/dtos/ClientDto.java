package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Клієнт")
public record ClientDto(
        @Schema(description = "ID клієнта", example = "1")
        Integer id,

        @NotBlank
        @Schema(description = "Ім'я", example = "Іван")
        String firstName,

        @NotBlank
        @Schema(description = "Прізвище", example = "Петренко")
        String lastName,

        @Schema(description = "По-батькові", example = "Олегович")
        String middleName,

        @NotBlank
        @Pattern(regexp = "^\\+380\\d{9}$", message = "Формат телефону: +380xxxxxxxxx")
        @Schema(description = "Телефон", example = "+380501234567")
        String phoneNumber,

        @Email
        @Schema(description = "Email", example = "ivan@mail.com")
        String email
) {}