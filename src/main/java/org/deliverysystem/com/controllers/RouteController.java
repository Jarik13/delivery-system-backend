package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.dtos.search.RouteSearchCriteria;
import org.deliverysystem.com.services.impl.RouteService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "Routes", description = "Шаблони магістральних маршрутів")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Operation(summary = "Отримати маршрути (з фільтрацією та пагінацією)")
    @GetMapping
    public ResponseEntity<Page<RouteDto>> getAll(
            @ParameterObject RouteSearchCriteria searchCriteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(routeService.findAll(searchCriteria, pageable));
    }

    @Operation(summary = "Знайти маршрути, що виїжджають з відділення")
    @GetMapping("branchId")
    public ResponseEntity<Page<RouteDto>> getByOriginBranch(@RequestParam Integer branchId, Pageable pageable) {
        return ResponseEntity.ok(routeService.findAllByOriginBranchId(branchId, pageable));
    }

    @Operation(summary = "Отримати маршрут за ID")
    @GetMapping("/{id}")
    public ResponseEntity<RouteDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @Operation(summary = "Створити маршрут")
    @ApiResponse(responseCode = "201", description = "Успішно створено")
    @PostMapping
    public ResponseEntity<RouteDto> create(@Valid @RequestBody RouteDto dto) {
        return new ResponseEntity<>(routeService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити маршрут")
    @PutMapping("/{id}")
    public ResponseEntity<RouteDto> update(@PathVariable Integer id, @Valid @RequestBody RouteDto dto) {
        return ResponseEntity.ok(routeService.update(id, dto));
    }

    @Operation(summary = "Видалити маршрут")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}