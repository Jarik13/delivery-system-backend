package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.routes.CreateRouteDto;
import org.deliverysystem.com.dtos.routes.RouteDto;
import org.deliverysystem.com.dtos.routes.RouteStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteSearchCriteria;
import org.deliverysystem.com.services.impl.RouteService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Шаблони магістральних маршрутів")
public class RouteController {
    private final RouteService routeService;

    @Operation(summary = "Отримати маршрути (з фільтрацією та пагінацією)")
    @GetMapping
    public ResponseEntity<RestPage<RouteDto>> getAll(
            @ParameterObject RouteSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(routeService.findAll(criteria, pageable));
    }

    @Operation(summary = "Отримати статистику по маршрутах (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<RouteStatisticsDto> getStatistics() {
        return ResponseEntity.ok(routeService.getStatistics());
    }

    @Operation(summary = "Знайти маршрути, що виїжджають з відділення")
    @GetMapping(params = "branchId")
    public ResponseEntity<RestPage<RouteDto>> getByOriginBranch(
            @RequestParam Integer branchId,
            @ParameterObject Pageable pageable
    ) {
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
    public ResponseEntity<RouteDto> create(@Valid @RequestBody CreateRouteDto dto) {
        return new ResponseEntity<>(routeService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити маршрут")
    @PutMapping("/{id}")
    public ResponseEntity<RouteDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateRouteDto dto
    ) {
        return ResponseEntity.ok(routeService.update(id, dto));
    }

    @Operation(summary = "Видалити маршрут")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}