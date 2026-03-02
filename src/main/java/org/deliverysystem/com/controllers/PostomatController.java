package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.postomats.PostomatDto;
import org.deliverysystem.com.dtos.postomats.PostomatStatisticsDto;
import org.deliverysystem.com.dtos.search.PostomatSearchCriteria;
import org.deliverysystem.com.services.impl.PostomatService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postomats")
@Tag(name = "Postomats", description = "Управління поштоматами")
@RequiredArgsConstructor
public class PostomatController {
    private final PostomatService postomatService;

    @Operation(summary = "Отримати поштомати (з фільтрацією та пагінацією)")
    @GetMapping
    public ResponseEntity<RestPage<PostomatDto>> getAll(
            @ParameterObject PostomatSearchCriteria searchCriteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(postomatService.findAll(searchCriteria, pageable));
    }

    @Operation(summary = "Отримати статистику по поштоматам (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<PostomatStatisticsDto> getStatistics() {
        return ResponseEntity.ok(postomatService.getStatistics());
    }

    @Operation(summary = "Знайти поштомати у місті")
    @GetMapping("cityId")
    public ResponseEntity<Page<PostomatDto>> getByCity(@RequestParam Integer cityId, Pageable pageable) {
        return ResponseEntity.ok(postomatService.findAllByCityId(cityId, pageable));
    }

    @Operation(summary = "Отримати поштомат за ID")
    @GetMapping("/{id}")
    public ResponseEntity<PostomatDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(postomatService.findById(id));
    }

    @Operation(summary = "Створити поштомат")
    @ApiResponse(responseCode = "201", description = "Успішно створено")
    @PostMapping
    public ResponseEntity<PostomatDto> create(@Valid @RequestBody PostomatDto dto) {
        return new ResponseEntity<>(postomatService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити поштомат")
    @PutMapping("/{id}")
    public ResponseEntity<PostomatDto> update(@PathVariable Integer id, @Valid @RequestBody PostomatDto dto) {
        return ResponseEntity.ok(postomatService.update(id, dto));
    }

    @Operation(summary = "Видалити поштомат")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        postomatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}