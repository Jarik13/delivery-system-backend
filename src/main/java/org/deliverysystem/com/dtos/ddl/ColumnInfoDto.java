package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Інформація про колонку")
public record ColumnInfoDto(
        String columnName,
        String dataType,
        Integer maxLength,
        Integer numericPrecision,
        Integer numericScale,
        boolean isNullable,
        String defaultValue
) {}