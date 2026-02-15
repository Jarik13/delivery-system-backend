package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.dtos.search.BranchSearchCriteria;
import org.deliverysystem.com.services.impl.BranchService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Управління відділеннями")
public class BranchController {
    private final BranchService branchService;

    @Operation(summary = "Отримати відділення (з фільтрацією та пагінацією)")
    @GetMapping
    public ResponseEntity<Page<BranchDto>> getAll(
            @ParameterObject BranchSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(branchService.findAll(criteria, pageable));
    }

    @Operation(summary = "Знайти відділення за містом")
    @GetMapping("cityId")
    public ResponseEntity<Page<BranchDto>> getByCity(@RequestParam Integer cityId, Pageable pageable) {
        return ResponseEntity.ok(branchService.findAllByCityId(cityId, pageable));
    }

    @Operation(summary = "Отримати відділення за ID")
    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @Operation(summary = "Створити нове відділення")
    @ApiResponse(responseCode = "201", description = "Успішно створено")
    @PostMapping
    public ResponseEntity<BranchDto> create(@Valid @RequestBody BranchDto dto) {
        return new ResponseEntity<>(branchService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити відділення")
    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> update(@PathVariable Integer id, @Valid @RequestBody BranchDto dto) {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @Operation(summary = "Видалити відділення")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}