package org.deliverysystem.com.dtos;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FleetDriveTypeDto(
        @Schema(description = "ID", example = "1")
        Integer id,

        @NotNull @Schema(description = "Тип приводу", example = "Задній")
        String name
) {}