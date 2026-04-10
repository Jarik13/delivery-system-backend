package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запит на видалення колонки")
public record DropColumnRequest(

        @NotBlank
        @Schema(description = "Назва таблиці", example = "shipments")
        String tableName,

        @NotBlank
        @Schema(description = "Назва колонки", example = "shipment_note")
        String columnName
) {}