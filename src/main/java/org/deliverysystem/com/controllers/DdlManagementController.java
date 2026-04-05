package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.ddl.*;
import org.deliverysystem.com.services.DdlManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ddl")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "DDL Management", description = "Керування структурою бази даних (тільки SUPER_ADMIN)")
public class DdlManagementController {
    private final DdlManagementService ddlService;

    @GetMapping("/tables")
    @Operation(summary = "Отримати список всіх таблиць")
    public ResponseEntity<List<String>> getAllTables() {
        return ResponseEntity.ok(ddlService.getAllTables());
    }

    @GetMapping("/tables/{tableName}")
    @Operation(summary = "Отримати структуру таблиці (колонки, constraints, індекси)")
    public ResponseEntity<TableInfoDto> getTableInfo(@PathVariable String tableName) {
        return ResponseEntity.ok(ddlService.getTableInfo(tableName));
    }

    @PostMapping("/tables")
    @Operation(summary = "Створити нову таблицю")
    public ResponseEntity<Void> createTable(@Valid @RequestBody CreateTableRequest request) {
        ddlService.createTable(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/columns")
    @Operation(summary = "Додати колонку до таблиці")
    public ResponseEntity<Void> addColumn(@Valid @RequestBody AddColumnRequest request) {
        ddlService.addColumn(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/columns")
    @Operation(summary = "Видалити колонку з таблиці")
    public ResponseEntity<Void> dropColumn(@Valid @RequestBody DropColumnRequest request) {
        ddlService.dropColumn(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/columns")
    @Operation(summary = "Змінити тип колонки")
    public ResponseEntity<Void> alterColumn(@Valid @RequestBody AlterColumnRequest request) {
        ddlService.alterColumn(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/columns/default")
    @Operation(summary = "Встановити або видалити дефолтне значення колонки")
    public ResponseEntity<Void> setDefault(@Valid @RequestBody SetDefaultRequest request) {
        ddlService.setDefault(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/constraints")
    @Operation(summary = "Додати constraint (NOT NULL, UNIQUE, CHECK)")
    public ResponseEntity<Void> addConstraint(@Valid @RequestBody AddConstraintRequest request) {
        ddlService.addConstraint(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/constraints")
    @Operation(summary = "Видалити constraint")
    public ResponseEntity<Void> dropConstraint(@Valid @RequestBody DropConstraintRequest request) {
        ddlService.dropConstraint(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/indexes")
    @Operation(summary = "Створити індекс")
    public ResponseEntity<Void> addIndex(@Valid @RequestBody AddIndexRequest request) {
        ddlService.addIndex(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/indexes")
    @Operation(summary = "Видалити індекс")
    public ResponseEntity<Void> dropIndex(@Valid @RequestBody DropIndexRequest request) {
        ddlService.dropIndex(request);
        return ResponseEntity.ok().build();
    }
}