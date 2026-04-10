package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.enums.ConstraintType;

@Schema(description = "Запит на додавання constraint")
public record AddConstraintRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotBlank
        @Schema(description = "Назва колонки")
        String columnName,

        @NotNull
        @Schema(description = "Тип constraint")
        ConstraintType constraintType,

        @Schema(description = "Вираз для CHECK constraint", example = "age > 0")
        String checkExpression,

        @Schema(description = "Назва constraint (опційно, генерується автоматично)")
        String constraintName
) {}