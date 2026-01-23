package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.deliverysystem.com.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class AbstractBaseController<D, ID> {
    protected final BaseService<D, ID> service;

    protected AbstractBaseController(BaseService<D, ID> service) {
        this.service = service;
    }

    @Operation(summary = "Отримати запис за ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запис знайдено"),
            @ApiResponse(responseCode = "404", description = "Запис не знайдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable ID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Отримати всі записи (з пагінацією)",
            description = "Використовуйте параметри page та size. Наприклад: ?page=0&size=10")
    @GetMapping
    public ResponseEntity<Page<D>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Створити новий запис")
    @ApiResponse(responseCode = "201", description = "Успішно створено")
    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити існуючий запис")
    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @Valid @RequestBody D dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Видалити запис")
    @ApiResponse(responseCode = "204", description = "Успішно видалено")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}