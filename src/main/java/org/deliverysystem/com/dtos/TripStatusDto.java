package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TripStatusDto(
        @Schema(description = "ID статусу рейсу", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва статусу", example = "Виїхав")
        String name
) {}