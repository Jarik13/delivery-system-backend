package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ReturnReasonDto(
        @Schema(description = "ID причини", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва причини повернення", example = "Відмова клієнта")
        String name
) {}