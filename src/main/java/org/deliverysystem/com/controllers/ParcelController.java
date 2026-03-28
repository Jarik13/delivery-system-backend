package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.parcels.ParcelDto;
import org.deliverysystem.com.dtos.parcels.ParcelStatisticsDto;
import org.deliverysystem.com.dtos.search.ParcelSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.services.impl.ParcelService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parcels")
@RequiredArgsConstructor
@Tag(name = "Parcels", description = "Управління посилками (параметри та умови зберігання)")
public class ParcelController {
    private final ParcelService parcelService;

    @Operation(summary = "Отримати всі посилки (з фільтрацією за вагою, вартістю та типом)")
    @GetMapping
    public ResponseEntity<RestPage<ParcelDto>> getAll(
            @ParameterObject ParcelSearchCriteria criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(parcelService.findAll(criteria, pageable, user));
    }

    @Operation(summary = "Посилки без прив'язаного відправлення")
    @GetMapping("/unshipped")
    public ResponseEntity<RestPage<ParcelDto>> getUnshipped(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(parcelService.findUnshipped(pageable));
    }

    @Operation(summary = "Отримати статистику по посилках (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<ParcelStatisticsDto> getStatistics() {
        return ResponseEntity.ok(parcelService.getStatistics());
    }

    @Operation(summary = "Отримати посилку за ID")
    @GetMapping("/{id}")
    public ResponseEntity<ParcelDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(parcelService.findById(id));
    }

    @Operation(summary = "Створити нову посилку")
    @ApiResponse(responseCode = "201", description = "Створено")
    @PostMapping
    public ResponseEntity<ParcelDto> create(@Valid @RequestBody ParcelDto dto) {
        return new ResponseEntity<>(parcelService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити параметри посилки")
    @PutMapping("/{id}")
    public ResponseEntity<ParcelDto> update(@PathVariable Integer id, @Valid @RequestBody ParcelDto dto) {
        return ResponseEntity.ok(parcelService.update(id, dto));
    }

    @Operation(summary = "Видалити посилку")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        parcelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}