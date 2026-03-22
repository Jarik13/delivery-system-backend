package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.returns.CreateReturnDto;
import org.deliverysystem.com.dtos.returns.CreateReturnResponseDto;
import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.dtos.returns.ReturnStatisticsDto;
import org.deliverysystem.com.dtos.search.ReturnSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.services.impl.ReturnService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/returns")
@Tag(name = "Returns", description = "Повернення")
@RequiredArgsConstructor
public class ReturnController {
    private final ReturnService returnService;

    @Operation(summary = "Отримати всі повернення (пагінація)")
    @GetMapping
    public ResponseEntity<RestPage<ReturnDto>> getAll(
            @ParameterObject ReturnSearchCriteria criteria,
            @ParameterObject Pageable pageable,
            @CurrentUser CurrentUserDto user
    ) {
        return ResponseEntity.ok(returnService.findAll(criteria, pageable, user));
    }

    @Operation(summary = "Отримати статистику по поверненням (мін/макс сума)")
    @GetMapping("/statistics")
    public ResponseEntity<ReturnStatisticsDto> getStatistics() {
        return ResponseEntity.ok(returnService.getStatistics());
    }

    @PostMapping
    @Operation(summary = "Оформити повернення")
    public ResponseEntity<CreateReturnResponseDto> create(@Valid @RequestBody CreateReturnDto dto) {
        return ResponseEntity.ok(returnService.create(dto));
    }
}
