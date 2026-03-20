package org.deliverysystem.com.dtos.employees;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Співробітник")
public record EmployeeDto(
        @Schema(description = "ID працівника", example = "1")
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

        @NotNull
        @Schema(description = "ID відділення, де працює", example = "5")
        Integer branchId
) {}