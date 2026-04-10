package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запит на встановлення дефолтного значення")
public record SetDefaultRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotBlank
        @Schema(description = "Назва колонки")
        String columnName,

        @Schema(description = "Дефолтне значення (null = видалити дефолт)")
        String defaultValue
) {}