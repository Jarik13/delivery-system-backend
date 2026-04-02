package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.route_lists.RouteListShipmentDto;
import org.deliverysystem.com.dtos.search.AvailableShipmentsCriteriaDto;
import org.deliverysystem.com.dtos.search.ShipmentSearchCriteria;
import org.deliverysystem.com.dtos.shipments.*;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.export.ShipmentExportContext;
import org.deliverysystem.com.services.impl.ShipmentService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "Керування відправленнями (ТТН)")
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final ShipmentExportContext exportContext;

    @Operation(summary = "Отримати всі відправлення з фільтрацією")
    @GetMapping
    public ResponseEntity<RestPage<ShipmentDto>> getAll(
            @ParameterObject ShipmentSearchCriteria criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(shipmentService.findAll(criteria, pageable, user));
    }

    @Operation(summary = "Отримати статистику по відправленням (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<ShipmentStatisticsDto> getStatistics() {
        return ResponseEntity.ok(shipmentService.getStatistics());
    }

    @Operation(summary = "Отримати історію руху відправлення за ID (трекінг)")
    @GetMapping("/{id}/movement")
    public ResponseEntity<List<ShipmentMovementDto>> getMovementHistory(@PathVariable Integer id) {
        return ResponseEntity.ok(shipmentService.getShipmentHistory(id));
    }

    @Operation(summary = "Отримати рекомендовані відправлення для сегмента маршруту")
    @GetMapping(value = "/suggested", params = "routeId")
    public ResponseEntity<List<ShipmentDto>> getSuggested(@RequestParam Integer routeId) {
        return ResponseEntity.ok(shipmentService.getSuggestedShipments(routeId));
    }

    @GetMapping("/available-for-segment")
    @Operation(summary = "Отримати відправлення, кінцева точка яких збігається з містом призначення сегмента")
    public ResponseEntity<RestPage<ShipmentDto>> getAvailableForSegment(
            @ParameterObject @Valid AvailableShipmentsCriteriaDto criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(shipmentService.findAvailableForSegment(criteria, pageable, user));
    }

    @Operation(summary = "Отримати відправлення доступні для додавання в маршрутний лист")
    @GetMapping("/available-for-route-list")
    public ResponseEntity<List<RouteListShipmentDto>> getAvailableForRouteList() {
        return ResponseEntity.ok(shipmentService.getAvailableForRouteList());
    }

    @Operation(summary = "Отримати за ID")
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @Operation(summary = "Експорт відправлень",
            description = "Формати: csv | xlsx | pdf | json")
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @Parameter(description = "Формат: csv | xlsx | pdf | json")
            @RequestParam String format,
            @Parameter(description = "Список конкретних ID (якщо не вказано — всі за фільтром)")
            @RequestParam(required = false) List<Integer> ids,
            @ParameterObject ShipmentSearchCriteria criteria,
            @CurrentUser CurrentUserDto user) {
        List<ShipmentDto> shipments = (ids != null && !ids.isEmpty())
                ? ids.stream().map(shipmentService::findById).toList()
                : shipmentService.findAll(criteria, Pageable.unpaged(), user).getContent();

        return exportContext.export(format, shipments);
    }

    @Operation(summary = "Розрахувати вартість доставки (Тарифікація)")
    @PostMapping("/calculate")
    public ResponseEntity<CalculatedPriceResponseDto> calculate(@Valid @RequestBody ShipmentPriceCalculationRequestDto request) {
        return ResponseEntity.ok(shipmentService.calculatePrices(request));
    }

    @Operation(summary = "Створити нове відправлення (ТТН)")
    @PostMapping
    public ResponseEntity<ShipmentDto> create(
            @Valid @RequestBody CreateShipmentDto dto,
            @CurrentUser Integer userId
    ) {
        return new ResponseEntity<>(shipmentService.createComplexShipment(dto, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити існуюче відправлення")
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateShipmentDto dto,
            @CurrentUser Integer userId) {
        return ResponseEntity.ok(shipmentService.updateComplexShipment(id, dto, userId));
    }

    @Operation(summary = "Видалити відправлення")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}