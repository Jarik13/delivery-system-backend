package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FleetBrandDto(
        @Schema(description = "ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Марка авто", example = "Mercedes")
        String name
) {}