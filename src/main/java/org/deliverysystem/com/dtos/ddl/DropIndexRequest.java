package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запит на видалення індексу")
public record DropIndexRequest(
        @NotBlank
        @Schema(description = "Назва таблиці")
        String tableName,

        @NotBlank
        @Schema(description = "Назва індексу")
        String indexName
) {}