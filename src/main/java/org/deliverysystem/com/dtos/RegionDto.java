package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Дані області")
public record RegionDto(
        @Schema(description = "Унікальний ID", example = "1")
        Integer id,

        @NotBlank
        @Schema(description = "Назва області", example = "Київська")
        String name
) {}