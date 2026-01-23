package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FleetTransmissionTypeDto(
        @Schema(description = "ID", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Тип КПП", example = "Механіка")
        String name
) {}