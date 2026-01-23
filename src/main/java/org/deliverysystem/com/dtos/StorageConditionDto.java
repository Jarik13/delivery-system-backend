package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StorageConditionDto(
        @Schema(description = "ID умови", example = "1")
        Integer id,

        @NotNull
        @Schema(description = "Назва умови", example = "Холодильник")
        String name,

        @Schema(description = "Технічний опис вимог", example = "Температура +2...+8")
        String description
) {}