package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Інформація про зовнішній ключ")
public record ForeignKeyInfoDto(
        String constraintName,
        String columnName,
        String referencedTable,
        String referencedColumn
) {}