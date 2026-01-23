package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ShipmentStatusDto(
        @Schema(description = "ID статусу", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва статусу", example = "У дорозі")
        String name
) {}