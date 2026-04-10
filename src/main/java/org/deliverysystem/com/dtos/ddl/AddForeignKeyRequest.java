package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запит на додавання зовнішнього ключа")
public record AddForeignKeyRequest(
        @NotBlank
        @Schema(description = "Таблиця до якої додається FK", example = "shipments")
        String tableName,

        @NotBlank
        @Schema(description = "Колонка яка буде FK", example = "client_id")
        String columnName,

        @NotBlank
        @Schema(description = "Таблиця на яку посилається FK", example = "clients")
        String referencedTable,

        @NotBlank
        @Schema(description = "Колонка на яку посилається FK", example = "client_id")
        String referencedColumn,

        @Schema(description = "Назва constraint (опційно)")
        String constraintName
) {}