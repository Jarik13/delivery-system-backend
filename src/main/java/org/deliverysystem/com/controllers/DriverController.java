package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.DriverDto;
import org.deliverysystem.com.services.impl.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers", description = "Водії магістральних перевезень")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping
    @Operation(summary = "Отримати всіх водіїв")
    public ResponseEntity<List<DriverDto>> getAll() {
        return ResponseEntity.ok(driverService.findAllWithActiveTripStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DriverDto> create(@Valid @RequestBody DriverDto dto) {
        return ResponseEntity.ok(driverService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDto> update(@PathVariable Integer id, @Valid @RequestBody DriverDto dto) {
        return ResponseEntity.ok(driverService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}