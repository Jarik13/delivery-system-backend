package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.CourierDto;
import org.deliverysystem.com.services.impl.CourierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/couriers")
@Tag(name = "Couriers", description = "Кур'єри адресної доставки")
@RequiredArgsConstructor
public class CourierController {
    private final CourierService courierService;

    @GetMapping
    @Operation(summary = "Отримати всіх кур'єрів")
    public ResponseEntity<List<CourierDto>> getAll() {
        return ResponseEntity.ok(courierService.findAllWithActiveRouteListStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courierService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CourierDto> create(@Valid @RequestBody CourierDto dto) {
        return ResponseEntity.ok(courierService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourierDto> update(@PathVariable Integer id, @Valid @RequestBody CourierDto dto) {
        return ResponseEntity.ok(courierService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}