package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Запит на створення індексу")
public record AddIndexRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotEmpty
        @Schema(description = "Колонки індексу")
        List<String> columnNames,

        @Schema(description = "Чи унікальний індекс")
        boolean unique,

        @Schema(description = "Назва індексу (опційно)")
        String indexName
) {}