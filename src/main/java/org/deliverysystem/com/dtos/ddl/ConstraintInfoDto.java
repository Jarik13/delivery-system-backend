package org.deliverysystem.com.dtos.ddl;

public record ConstraintInfoDto(
        String constraintName,
        String constraintType,
        String columnName,
        String checkClause
) {}