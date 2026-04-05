package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Запит на створення нової таблиці")
public record CreateTableRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotEmpty
        @Schema(description = "Колонки таблиці")
        List<AddColumnRequest> columns,

        @Schema(description = "Назва колонки первинного ключа", example = "id")
        String primaryKeyColumn
) {}