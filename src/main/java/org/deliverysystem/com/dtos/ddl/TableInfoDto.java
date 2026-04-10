package org.deliverysystem.com.dtos.ddl;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Інформація про таблицю")
public record TableInfoDto(
        String tableName,
        List<ColumnInfoDto> columns,
        List<ConstraintInfoDto> constraints,
        List<IndexInfoDto> indexes,
        List<ForeignKeyInfoDto> foreignKeys
) {}