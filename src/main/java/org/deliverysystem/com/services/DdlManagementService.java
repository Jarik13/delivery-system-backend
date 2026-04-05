package org.deliverysystem.com.services;

import org.deliverysystem.com.dtos.ddl.*;

import java.util.List;

public interface DdlManagementService {
    List<String> getAllTables();
    TableInfoDto getTableInfo(String tableName);
    void createTable(CreateTableRequest request);

    void addColumn(AddColumnRequest request);
    void dropColumn(DropColumnRequest request);
    void alterColumn(AlterColumnRequest request);

    void setDefault(SetDefaultRequest request);

    void addConstraint(AddConstraintRequest request);
    void dropConstraint(DropConstraintRequest request);


    void addIndex(AddIndexRequest request);
    void dropIndex(DropIndexRequest request);
}