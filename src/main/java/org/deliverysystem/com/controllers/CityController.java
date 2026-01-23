package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.CityDto;
import org.deliverysystem.com.services.impl.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@Tag(name = "Cities", description = "Довідник міст")
public class CityController extends AbstractBaseController<CityDto, Integer> {
    private final CityService cityService;

    public CityController(CityService service) {
        super(service);
        this.cityService = service;
    }

    @Operation(summary = "Отримати міста району (з пагінацією)")
    @GetMapping("/by-district/{districtId}")
    public ResponseEntity<Page<CityDto>> getByDistrict(@PathVariable Integer districtId, Pageable pageable) {
        return ResponseEntity.ok(cityService.findAllByDistrictId(districtId, pageable));
    }
}