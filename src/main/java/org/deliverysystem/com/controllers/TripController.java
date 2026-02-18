package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.search.TripSearchCriteria;
import org.deliverysystem.com.services.impl.TripService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trips")
@Tag(name = "Trips", description = "Управління магістральними рейсами")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping
    @Operation(summary = "Отримати список рейсів з фільтрацією")
    public ResponseEntity<RestPage<TripDto>> getAll(
            @ParameterObject TripSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(tripService.findAll(criteria, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати деталі рейсу")
    public ResponseEntity<TripDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Створити новий рейс")
    public ResponseEntity<TripDto> create(@RequestBody TripDto dto) {
        return ResponseEntity.ok(tripService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити дані рейсу")
    public ResponseEntity<TripDto> update(@PathVariable Integer id, @RequestBody TripDto dto) {
        return ResponseEntity.ok(tripService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити рейс")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
}