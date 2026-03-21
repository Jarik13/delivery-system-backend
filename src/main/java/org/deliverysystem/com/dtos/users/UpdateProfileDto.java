package org.deliverysystem.com.dtos.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Дані для оновлення профілю")
public record UpdateProfileDto(
        @Schema(description = "Ім'я")
        String firstName,

        @Schema(description = "Прізвище")
        String lastName,

        @Schema(description = "По батькові")
        String middleName,

        @Pattern(regexp = "^$|^(\\+380|0)[0-9]{9}$", message = "Невірний формат телефону")
        @Schema(description = "Телефон")
        String phoneNumber,

        @Schema(description = "Номер ліцензії (тільки для DRIVER)")
        String licenseNumber
) {}