package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.enums.ColumnDataType;

@Schema(description = "Запит на зміну типу колонки")
public record AlterColumnRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotBlank
        @Schema(description = "Назва колонки")
        String columnName,

        @NotNull
        @Schema(description = "Новий тип даних")
        ColumnDataType dataType,

        @Schema(description = "Довжина")
        Integer length,

        @Schema(description = "Точність")
        Integer precision,

        @Schema(description = "Масштаб")
        Integer scale,

        @Schema(description = "Чи дозволено NULL")
        boolean nullable
) {}