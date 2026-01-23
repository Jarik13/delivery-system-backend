package org.deliverysystem.com.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Дані вулиці")
public record StreetDto(
        @Schema(description = "Унікальний ID", example = "100500")
        Integer id,

        @NotBlank
        @Schema(description = "Назва вулиці", example = "Хрещатик")
        String name,

        @NotNull
        @Schema(description = "ID міста", example = "55")
        Integer cityId
) {}