package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.CityDto;
import org.deliverysystem.com.dtos.search.CitySearchCriteria;
import org.deliverysystem.com.services.impl.CityService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@Tag(name = "Cities", description = "Довідник міст")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping
    @Operation(summary = "Отримати список міст з фільтрацією")
    public ResponseEntity<RestPage<CityDto>> getAll(
            @ParameterObject CitySearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(cityService.findAll(criteria, pageable));
    }

    @Operation(summary = "Отримати міста за ID району")
    @GetMapping(params = "districtId")
    public ResponseEntity<Page<CityDto>> getByDistrict(@RequestParam Integer districtId, Pageable pageable) {
        return ResponseEntity.ok(cityService.findAllByDistrictId(districtId, pageable));
    }
}