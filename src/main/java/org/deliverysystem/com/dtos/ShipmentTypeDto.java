package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ShipmentTypeDto(
        @Schema(description = "ID типу відправлення", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва типу", example = "Експрес")
        String name
) {}