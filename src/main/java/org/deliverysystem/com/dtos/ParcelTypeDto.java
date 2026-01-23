package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ParcelTypeDto(
        @Schema(description = "ID типу вмісту", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва типу вмісту", example = "Документи")
        String name
) {}