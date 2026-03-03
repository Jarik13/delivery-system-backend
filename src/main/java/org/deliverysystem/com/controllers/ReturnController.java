package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.dtos.search.ReturnSearchCriteria;
import org.deliverysystem.com.services.impl.ReturnService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(returnService.findAll(criteria, pageable));
    }
}
