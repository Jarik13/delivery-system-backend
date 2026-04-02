package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.route_lists.*;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.export.RouteListExportContext;
import org.deliverysystem.com.services.impl.RouteListService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/route-lists")
@RequiredArgsConstructor
@Tag(name = "Route Lists", description = "Маршрутні листи кур'єрів")
public class RouteListController {
    private final RouteListService routeListService;
    private final RouteListExportContext exportContext;

    @GetMapping
    @Operation(summary = "Отримати всі маршрутні листи з фільтрацією та пагінацією")
    public ResponseEntity<RestPage<RouteListDto>> getAll(
            @ParameterObject RouteListSearchCriteria criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
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
    public ResponseEntity<RouteListDto> create(
            @Valid @RequestBody CreateRouteListDto dto,
            @CurrentUser CurrentUserDto user
    ) {
        return new ResponseEntity<>(routeListService.createRouteList(dto, user.id()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити дані маршрутного листа (тільки у статусі 'Сформовано')")
    public ResponseEntity<RouteListDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRouteListDto dto) {
        return ResponseEntity.ok(routeListService.updateRouteList(id, dto));
    }

    @PatchMapping("/items/{itemId}/status")
    @Operation(summary = "Оновити статус відправлення")
    public ResponseEntity<RouteSheetItemDto> updateShipmentStatus(
            @PathVariable Integer itemId,
            @RequestBody UpdateShipmentDeliveryStatusDto dto) {
        return ResponseEntity.ok(routeListService.updateShipmentDeliveryStatus(itemId, dto.action()));
    }

    @PatchMapping("/{id}/shipments")
    @Operation(summary = "Додати відправлення до існуючого маршрутного листа")
    public ResponseEntity<RouteListDto> addShipments(
            @PathVariable Integer id,
            @Valid @RequestBody AddShipmentsToRouteListDto dto) {
        return ResponseEntity.ok(routeListService.addShipments(id, dto));
    }

    @PatchMapping("/{id}/accept")
    @Operation(summary = "Прийняти маршрутний лист (статус: Видано кур'єру)")
    public ResponseEntity<RouteListDto> accept(@PathVariable Integer id) {
        return ResponseEntity.ok(routeListService.acceptRouteList(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити маршрутний лист")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeListService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Експорт маршрутних листів",
            description = "Формати: csv | xlsx | pdf | json")
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @Parameter(description = "Формат: csv | xlsx | pdf | json")
            @RequestParam String format,
            @Parameter(description = "Фільтр за номером")
            @RequestParam(required = false) Integer number,
            @Parameter(description = "Список конкретних ID")
            @RequestParam(required = false) List<Integer> ids,
            @CurrentUser CurrentUserDto currentUser) {
        List<RouteListDto> routeLists = (ids != null && !ids.isEmpty())
                ? routeListService.findAllByIds(ids)
                : routeListService.findAllForExport(number, currentUser);

        return exportContext.export(format, routeLists);
    }
}