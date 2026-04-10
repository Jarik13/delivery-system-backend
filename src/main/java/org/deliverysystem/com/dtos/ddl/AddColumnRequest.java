package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.enums.ColumnDataType;

@Schema(description = "Запит на додавання колонки")
public record AddColumnRequest(
        @NotBlank
        @Schema(description = "Назва таблиці", example = "shipments")
        String tableName,

        @NotBlank
        @Schema(description = "Назва нової колонки", example = "shipment_note")
        String columnName,

        @NotNull
        @Schema(description = "Тип даних", example = "NVARCHAR")
        ColumnDataType dataType,

        @Schema(description = "Довжина (для VARCHAR/NVARCHAR/DECIMAL)", example = "255")
        Integer length,

        @Schema(description = "Точність (для DECIMAL/NUMERIC)", example = "10")
        Integer precision,

        @Schema(description = "Масштаб (для DECIMAL/NUMERIC)", example = "2")
        Integer scale,

        @Schema(description = "Чи дозволено NULL", example = "true")
        boolean nullable,

        @Schema(description = "Дефолтне значення", example = "0")
        String defaultValue
) {}