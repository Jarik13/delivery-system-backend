package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record VehicleActivityStatusDto(
        @Schema(description = "ID статусу", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Експлуатаційний стан", example = "В ремонті")
        String name
) {}