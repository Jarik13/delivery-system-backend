package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.services.impl.RouteListService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/route-lists")
@RequiredArgsConstructor
@Tag(name = "Route Lists", description = "Маршрутні листи кур'єрів")
public class RouteListController {
    private final RouteListService routeListService;

    @GetMapping
    @Operation(summary = "Отримати всі маршрутні листи з фільтрацією та пагінацією")
    public ResponseEntity<RestPage<RouteListDto>> getAll(
            @ParameterObject RouteListSearchCriteria criteria,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(routeListService.findAll(criteria, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати маршрутний лист за ID")
    public ResponseEntity<RouteListDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeListService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Створити новий маршрутний лист")
    public ResponseEntity<RouteListDto> create(@Valid @RequestBody RouteListDto dto) {
        return new ResponseEntity<>(routeListService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити існуючий маршрутний лист")
    public ResponseEntity<RouteListDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody RouteListDto dto) {
        return ResponseEntity.ok(routeListService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити маршрутний лист")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}