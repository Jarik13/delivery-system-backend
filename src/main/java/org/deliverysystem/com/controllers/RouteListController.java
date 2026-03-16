package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.route_lists.CreateRouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListStatisticsDto;
import org.deliverysystem.com.dtos.route_lists.UpdateShipmentDeliveryStatusDto;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
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
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user) {
        return ResponseEntity.ok(routeListService.findAll(criteria, pageable, user));
    }

    @Operation(summary = "Статистика накладних (діапазони для фільтрів)")
    @GetMapping("/statistics")
    public ResponseEntity<RouteListStatisticsDto> getStatistics() {
        return ResponseEntity.ok(routeListService.getStatistics());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати маршрутний лист за ID")
    public ResponseEntity<RouteListDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeListService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Створити новий маршрутний лист")
    public ResponseEntity<RouteListDto> create(@Valid @RequestBody CreateRouteListDto dto) {
        return new ResponseEntity<>(routeListService.createRouteList(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити існуючий маршрутний лист")
    public ResponseEntity<RouteListDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody RouteListDto dto) {
        return ResponseEntity.ok(routeListService.update(id, dto));
    }

    @PatchMapping("/items/{itemId}/status")
    public ResponseEntity<Void> updateShipmentStatus(
            @PathVariable Integer itemId,
            @RequestBody UpdateShipmentDeliveryStatusDto dto) {
        routeListService.updateShipmentDeliveryStatus(itemId, dto.action());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити маршрутний лист")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}