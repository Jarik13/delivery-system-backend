package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.dtos.ddl.*;
import org.deliverysystem.com.enums.ColumnDataType;
import org.deliverysystem.com.enums.ConstraintType;
import org.deliverysystem.com.services.DdlManagementService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DdlManagementServiceImpl implements DdlManagementService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAllTables() {
        return jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME",
                String.class
        );
    }

    @Override
    public TableInfoDto getTableInfo(String tableName) {
        validateIdentifier(tableName);

        List<ColumnInfoDto> columns = jdbcTemplate.query(
                """
                        SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH,
                               NUMERIC_PRECISION, NUMERIC_SCALE, IS_NULLABLE, COLUMN_DEFAULT
                        FROM INFORMATION_SCHEMA.COLUMNS
                        WHERE TABLE_NAME = ?
                        ORDER BY ORDINAL_POSITION
                        """,
                (rs, i) -> new ColumnInfoDto(
                        rs.getString("COLUMN_NAME"),
                        rs.getString("DATA_TYPE"),
                        rs.getObject("CHARACTER_MAXIMUM_LENGTH", Integer.class),
                        rs.getObject("NUMERIC_PRECISION", Integer.class),
                        rs.getObject("NUMERIC_SCALE", Integer.class),
                        "YES".equals(rs.getString("IS_NULLABLE")),
                        rs.getString("COLUMN_DEFAULT")
                ),
                tableName
        );

        List<ConstraintInfoDto> constraints = jdbcTemplate.query(
                """
                        SELECT tc.CONSTRAINT_NAME, tc.CONSTRAINT_TYPE,
                               kcu.COLUMN_NAME, cc.CHECK_CLAUSE
                        FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc
                        LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
                            ON tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME
                            AND tc.TABLE_NAME = kcu.TABLE_NAME
                        LEFT JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS cc
                            ON tc.CONSTRAINT_NAME = cc.CONSTRAINT_NAME
                        WHERE tc.TABLE_NAME = ?
                          AND tc.CONSTRAINT_TYPE IN ('UNIQUE','CHECK')
                        """,
                (rs, i) -> new ConstraintInfoDto(
                        rs.getString("CONSTRAINT_NAME"),
                        rs.getString("CONSTRAINT_TYPE"),
                        rs.getString("COLUMN_NAME"),
                        rs.getString("CHECK_CLAUSE")
                ),
                tableName
        );

        List<IndexInfoDto> indexes = jdbcTemplate.query(
                """
                        SELECT i.name AS index_name, i.is_unique,
                               STRING_AGG(c.name, ', ') WITHIN GROUP (ORDER BY ic.key_ordinal) AS columns
                        FROM sys.indexes i
                        JOIN sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
                        JOIN sys.columns c ON ic.object_id = c.object_id AND ic.column_id = c.column_id
                        JOIN sys.tables t ON i.object_id = t.object_id
                        WHERE t.name = ? AND i.is_primary_key = 0 AND i.type > 0
                        GROUP BY i.name, i.is_unique
                        """,
                (rs, i) -> new IndexInfoDto(
                        rs.getString("index_name"),
                        Arrays.asList(rs.getString("columns").split(", ")),
                        rs.getBoolean("is_unique")
                ),
                tableName
        );

        List<ForeignKeyInfoDto> foreignKeys = jdbcTemplate.query(
                """
                        SELECT fk.name AS constraint_name,
                               c.name  AS column_name,
                               rt.name AS referenced_table,
                               rc.name AS referenced_column
                        FROM sys.foreign_keys fk
                        JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
                        JOIN sys.columns c  ON fkc.parent_object_id   = c.object_id  AND fkc.parent_column_id   = c.column_id
                        JOIN sys.columns rc ON fkc.referenced_object_id = rc.object_id AND fkc.referenced_column_id = rc.column_id
                        JOIN sys.tables  pt ON fk.parent_object_id   = pt.object_id
                        JOIN sys.tables  rt ON fk.referenced_object_id = rt.object_id
                        WHERE pt.name = ?
                        """,
                (rs, i) -> new ForeignKeyInfoDto(
                        rs.getString("constraint_name"),
                        rs.getString("column_name"),
                        rs.getString("referenced_table"),
                        rs.getString("referenced_column")
                ),
                tableName
        );

        return new TableInfoDto(tableName, columns, constraints, indexes, foreignKeys);
    }

    @Override
    public void createTable(CreateTableRequest req) {
        validateIdentifier(req.tableName());

        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(quote(req.tableName())).append(" (\n");

        String pkCol = req.primaryKeyColumn() != null ? req.primaryKeyColumn() : req.tableName() + "_id";
        validateIdentifier(pkCol);
        sql.append("    ").append(quote(pkCol))
                .append(" INT IDENTITY(1,1) NOT NULL,\n");

        for (AddColumnRequest col : req.columns()) {
            validateIdentifier(col.columnName());
            sql.append("    ").append(quote(col.columnName()))
                    .append(" ").append(buildColumnType(col.dataType(), col.length(), col.precision(), col.scale()));

            if (!col.nullable()) sql.append(" NOT NULL");
            if (col.defaultValue() != null && !col.defaultValue().isBlank()) {
                sql.append(" DEFAULT ").append(sanitizeDefault(col.defaultValue()));
            }
            sql.append(",\n");
        }

        sql.append("    CONSTRAINT ").append(quote("PK_" + req.tableName()))
                .append(" PRIMARY KEY (").append(quote(pkCol)).append(")\n");
        sql.append(")");

        log.info("DDL createTable: {}", sql);
        jdbcTemplate.execute(sql.toString());
    }

    @Override
    public void dropTable(String tableName) {
        validateIdentifier(tableName);

        List<String> references = jdbcTemplate.queryForList(
                """
                        SELECT OBJECT_NAME(fk.parent_object_id) AS referencing_table
                        FROM sys.foreign_keys fk
                        JOIN sys.tables t ON fk.referenced_object_id = t.object_id
                        WHERE t.name = ?
                        """,
                String.class,
                tableName
        );

        if (!references.isEmpty()) {
            throw new IllegalStateException(
                    "Не можна видалити таблицю \"" + tableName + "\": на неї посилаються таблиці: "
                    + String.join(", ", references)
                    + ". Спочатку видаліть зовнішні ключі."
            );
        }

        String sql = "DROP TABLE " + quote(tableName);
        log.info("DDL dropTable: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void addColumn(AddColumnRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());

        String typeDef = buildColumnType(req.dataType(), req.length(), req.precision(), req.scale());
        String nullability = req.nullable() ? "NULL" : "NOT NULL";

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(quote(req.tableName()))
                .append(" ADD ").append(quote(req.columnName()))
                .append(" ").append(typeDef)
                .append(" ").append(nullability);

        if (req.defaultValue() != null && !req.defaultValue().isBlank()) {
            sql.append(" DEFAULT ").append(sanitizeDefault(req.defaultValue()));
        }

        log.info("DDL addColumn: {}", sql);
        jdbcTemplate.execute(sql.toString());
    }

    @Override
    public void dropColumn(DropColumnRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());

        dropDefaultConstraintIfExists(req.tableName(), req.columnName());

        String sql = "ALTER TABLE " + quote(req.tableName()) +
                     " DROP COLUMN " + quote(req.columnName());

        log.info("DDL dropColumn: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void alterColumn(AlterColumnRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());

        dropDefaultConstraintIfExists(req.tableName(), req.columnName());

        String typeDef = buildColumnType(req.dataType(), req.length(), req.precision(), req.scale());
        String nullability = req.nullable() ? "NULL" : "NOT NULL";

        String sql = "ALTER TABLE " + quote(req.tableName()) +
                     " ALTER COLUMN " + quote(req.columnName()) +
                     " " + typeDef + " " + nullability;

        log.info("DDL alterColumn: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void setDefault(SetDefaultRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());

        dropDefaultConstraintIfExists(req.tableName(), req.columnName());

        if (req.defaultValue() != null && !req.defaultValue().isBlank()) {
            String constraintName = "DF_" + req.tableName() + "_" + req.columnName();
            String sql = "ALTER TABLE " + quote(req.tableName()) +
                         " ADD CONSTRAINT " + quote(constraintName) +
                         " DEFAULT " + sanitizeDefault(req.defaultValue()) +
                         " FOR " + quote(req.columnName());

            log.info("DDL setDefault: {}", sql);
            jdbcTemplate.execute(sql);
        }
    }

    @Override
    public void addConstraint(AddConstraintRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());

        String constraintName = req.constraintName() != null && !req.constraintName().isBlank()
                ? req.constraintName()
                : generateConstraintName(req.constraintType(), req.tableName(), req.columnName());

        validateIdentifier(constraintName);

        String sql = switch (req.constraintType()) {
            case NOT_NULL -> {
                String colType = getColumnType(req.tableName(), req.columnName());
                yield "ALTER TABLE " + quote(req.tableName()) +
                      " ALTER COLUMN " + quote(req.columnName()) +
                      " " + colType + " NOT NULL";
            }
            case UNIQUE -> "ALTER TABLE " + quote(req.tableName()) +
                           " ADD CONSTRAINT " + quote(constraintName) +
                           " UNIQUE (" + quote(req.columnName()) + ")";
            case CHECK -> {
                if (req.checkExpression() == null || req.checkExpression().isBlank()) {
                    throw new IllegalArgumentException("checkExpression є обов'язковим для CHECK constraint");
                }
                yield "ALTER TABLE " + quote(req.tableName()) +
                      " ADD CONSTRAINT " + quote(constraintName) +
                      " CHECK (" + req.checkExpression() + ")";
            }
        };

        log.info("DDL addConstraint: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropConstraint(DropConstraintRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.constraintName());

        String sql = "ALTER TABLE " + quote(req.tableName()) +
                     " DROP CONSTRAINT " + quote(req.constraintName());

        log.info("DDL dropConstraint: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void addIndex(AddIndexRequest req) {
        validateIdentifier(req.tableName());
        req.columnNames().forEach(this::validateIdentifier);

        String indexName = req.indexName() != null && !req.indexName().isBlank()
                ? req.indexName()
                : "IX_" + req.tableName() + "_" + String.join("_", req.columnNames());

        validateIdentifier(indexName);

        String cols = req.columnNames().stream()
                .map(this::quote)
                .reduce((a, b) -> a + ", " + b)
                .orElseThrow();

        String unique = req.unique() ? "UNIQUE " : "";
        String sql = "CREATE " + unique + "INDEX " + quote(indexName) +
                     " ON " + quote(req.tableName()) + " (" + cols + ")";

        log.info("DDL addIndex: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropIndex(DropIndexRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.indexName());

        String sql = "DROP INDEX " + quote(req.indexName()) +
                     " ON " + quote(req.tableName());

        log.info("DDL dropIndex: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void addForeignKey(AddForeignKeyRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.columnName());
        validateIdentifier(req.referencedTable());
        validateIdentifier(req.referencedColumn());

        boolean columnExists = !jdbcTemplate.queryForList(
                "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?",
                req.tableName(), req.columnName()
        ).isEmpty();

        if (!columnExists) {
            String addCol = "ALTER TABLE " + quote(req.tableName()) +
                            " ADD " + quote(req.columnName()) + " INT NULL";
            log.info("DDL addForeignKey — auto-creating column: {}", addCol);
            jdbcTemplate.execute(addCol);
        }

        String constraintName = req.constraintName() != null && !req.constraintName().isBlank()
                ? req.constraintName()
                : "FK_" + req.tableName() + "_" + req.columnName();

        validateIdentifier(constraintName);

        String sql = "ALTER TABLE " + quote(req.tableName()) +
                     " ADD CONSTRAINT " + quote(constraintName) +
                     " FOREIGN KEY (" + quote(req.columnName()) + ")" +
                     " REFERENCES " + quote(req.referencedTable()) +
                     " (" + quote(req.referencedColumn()) + ")";

        log.info("DDL addForeignKey: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public void dropForeignKey(DropForeignKeyRequest req) {
        validateIdentifier(req.tableName());
        validateIdentifier(req.constraintName());

        String sql = "ALTER TABLE " + quote(req.tableName()) +
                     " DROP CONSTRAINT " + quote(req.constraintName());

        log.info("DDL dropForeignKey: {}", sql);
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<String> getReferencedColumns(String tableName) {
        validateIdentifier(tableName);
        return jdbcTemplate.queryForList(
                """
                        SELECT c.name
                        FROM sys.columns c
                        JOIN sys.indexes i ON i.object_id = c.object_id AND i.is_primary_key = 1
                        JOIN sys.index_columns ic ON ic.object_id = i.object_id
                            AND ic.index_id = i.index_id AND ic.column_id = c.column_id
                        JOIN sys.tables t ON t.object_id = c.object_id
                        WHERE t.name = ?
                        """,
                String.class,
                tableName
        );
    }

    private String buildColumnType(ColumnDataType type, Integer length, Integer precision, Integer scale) {
        return switch (type) {
            case VARCHAR -> "VARCHAR(" + (length != null ? length : 255) + ")";
            case NVARCHAR -> "NVARCHAR(" + (length != null ? length : 255) + ")";
            case DECIMAL, NUMERIC -> {
                int p = precision != null ? precision : 18;
                int s = scale != null ? scale : 2;
                yield type.name() + "(" + p + ", " + s + ")";
            }
            default -> type.name();
        };
    }

    private String getColumnType(String tableName, String columnName) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT DATA_TYPE +
                            CASE
                                WHEN CHARACTER_MAXIMUM_LENGTH IS NOT NULL
                                    THEN '(' + CAST(CHARACTER_MAXIMUM_LENGTH AS VARCHAR) + ')'
                                WHEN NUMERIC_PRECISION IS NOT NULL AND DATA_TYPE IN ('decimal','numeric')
                                    THEN '(' + CAST(NUMERIC_PRECISION AS VARCHAR) + ',' + CAST(NUMERIC_SCALE AS VARCHAR) + ')'
                                ELSE ''
                            END
                        FROM INFORMATION_SCHEMA.COLUMNS
                        WHERE TABLE_NAME = ? AND COLUMN_NAME = ?
                        """,
                String.class,
                tableName, columnName
        );
    }

    private void dropDefaultConstraintIfExists(String tableName, String columnName) {
        List<String> defaults = jdbcTemplate.queryForList(
                """
                        SELECT dc.name
                        FROM sys.default_constraints dc
                        JOIN sys.columns c ON dc.parent_object_id = c.object_id
                            AND dc.parent_column_id = c.column_id
                        JOIN sys.tables t ON c.object_id = t.object_id
                        WHERE t.name = ? AND c.name = ?
                        """,
                String.class,
                tableName, columnName
        );

        for (String constraintName : defaults) {
            jdbcTemplate.execute(
                    "ALTER TABLE " + quote(tableName) + " DROP CONSTRAINT " + quote(constraintName)
            );
        }
    }

    private String generateConstraintName(ConstraintType type, String table, String column) {
        String prefix = switch (type) {
            case NOT_NULL -> "NN";
            case UNIQUE -> "UQ";
            case CHECK -> "CK";
        };
        return prefix + "_" + table + "_" + column;
    }

    private void validateIdentifier(String name) {
        if (name == null || !name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException(
                    "Недозволена назва ідентифікатора: " + name
            );
        }
    }

    private String quote(String identifier) {
        return "[" + identifier.replace("]", "]]") + "]";
    }

    private String sanitizeDefault(String value) {
        if (value.matches("^-?\\d+(\\.\\d+)?$")) return value;

        if (value.equalsIgnoreCase("true")) return "1";
        if (value.equalsIgnoreCase("false")) return "0";

        if (value.startsWith("'") && value.endsWith("'")) {
            return "(N" + value + ")";
        }

        if (value.matches("^[A-Z_]+\\(\\)$")) return value;

        throw new IllegalArgumentException(
                "Недозволене дефолтне значення: " + value +
                ". Використовуй число, 'рядок', або SQL функцію типу GETDATE()"
        );
    }
}