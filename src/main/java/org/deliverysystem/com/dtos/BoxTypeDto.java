package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record BoxTypeDto(
        @Schema(description = "ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва типу коробки", example = "M")
        String name
) {}