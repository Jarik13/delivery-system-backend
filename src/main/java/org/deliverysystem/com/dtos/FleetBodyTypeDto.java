package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FleetBodyTypeDto(
        @Schema(description = "ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Тип кузова", example = "Тент")
        String name
) {}