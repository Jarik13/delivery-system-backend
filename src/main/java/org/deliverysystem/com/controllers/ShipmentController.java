package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.search.ShipmentSearchCriteria;
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.dtos.shipments.ShipmentStatisticsDto;
import org.deliverysystem.com.services.impl.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Tag(name = "Shipments", description = "Керування відправленнями (ТТН)")
public class ShipmentController {
    private final ShipmentService shipmentService;

    @Operation(summary = "Отримати всі відправлення з фільтрацією")
    @GetMapping
    public ResponseEntity<Page<ShipmentDto>> getAll(ShipmentSearchCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(shipmentService.findAll(criteria, pageable));
    }

    @Operation(summary = "Отримати статистику по відправленням (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<ShipmentStatisticsDto> getStatistics() {
        return ResponseEntity.ok(shipmentService.getStatistics());
    }

    @Operation(summary = "Отримати за ID")
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @Operation(summary = "Створити нове відправлення (ТТН)")
    @PostMapping
    public ResponseEntity<ShipmentDto> create(@Valid @RequestBody ShipmentDto dto) {
        return new ResponseEntity<>(shipmentService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити існуюче відправлення")
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDto> update(@PathVariable Integer id, @Valid @RequestBody ShipmentDto dto) {
        return ResponseEntity.ok(shipmentService.update(id, dto));
    }

    @Operation(summary = "Видалити відправлення")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        shipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Знайти за трек-номером")
    @GetMapping("/tracking/{tracking}")
    public ResponseEntity<ShipmentDto> findByTracking(@PathVariable String tracking) {
        return ResponseEntity.ok(shipmentService.findByTrackingNumber(tracking));
    }

    @Operation(summary = "Історія відправлень конкретного клієнта")
    @GetMapping("senderId")
    public ResponseEntity<Page<ShipmentDto>> getBySender(@RequestParam Integer senderId, Pageable pageable) {
        return ResponseEntity.ok(shipmentService.findAllBySenderId(senderId, pageable));
    }
}