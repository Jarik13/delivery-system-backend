package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запит на видалення constraint")
public record DropConstraintRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotBlank
        @Schema(description = "Назва constraint")
        String constraintName
) {}