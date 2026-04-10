package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.trips.CreateTripDto;
import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.search.TripSearchCriteria;
import org.deliverysystem.com.dtos.trips.TripSegmentDto;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.services.impl.TripService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(tripService.findAll(criteria, pageable, user));
    }

    @GetMapping("/{id}/segments")
    @Operation(summary = "Отримати сегменти маршруту рейсу")
    public ResponseEntity<List<TripSegmentDto>> getSegments(@PathVariable Integer id) {
        return ResponseEntity.ok(tripService.getSegments(id));
    }

    @GetMapping("/by-branch")
    @Operation(summary = "Отримати рейси, що проходять через відділення працівника")
    public ResponseEntity<RestPage<TripDto>> getByBranch(
            @ParameterObject TripSearchCriteria criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(tripService.findAllByEmployeeBranch(criteria, pageable, user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати деталі рейсу")
    public ResponseEntity<TripDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Створити новий рейс")
    public ResponseEntity<TripDto> create(@Valid @RequestBody CreateTripDto dto) {
        return ResponseEntity.ok(tripService.createTrip(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Оновити дані рейсу")
    public ResponseEntity<TripDto> update(@PathVariable Integer id, @RequestBody TripDto dto) {
        return ResponseEntity.ok(tripService.update(id, dto));
    }

    @PatchMapping("/waybill-routes/{waybillRouteId}/arrive")
    public ResponseEntity<Void> markArrived(@PathVariable Integer waybillRouteId) {
        tripService.markArrived(waybillRouteId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/waybill-routes/{waybillRouteId}/depart")
    public ResponseEntity<Void> markDeparted(@PathVariable Integer waybillRouteId) {
        tripService.markDeparted(waybillRouteId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/emergency-stop")
    @Operation(summary = "Аварійна зупинка рейсу")
    public ResponseEntity<Void> emergencyStop(@PathVariable Integer id) {
        tripService.emergencyStop(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Видалити рейс")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
}