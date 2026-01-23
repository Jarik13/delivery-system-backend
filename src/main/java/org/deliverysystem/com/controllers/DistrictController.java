package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.DistrictDto;
import org.deliverysystem.com.services.impl.DistrictService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/districts")
@Tag(name = "Districts", description = "Довідник районів")
public class DistrictController extends AbstractBaseController<DistrictDto, Integer> {
    private final DistrictService districtService;

    public DistrictController(DistrictService service) {
        super(service);
        this.districtService = service;
    }

    @Operation(summary = "Отримати райони за ID області")
    @GetMapping(params = "regionId")
    public ResponseEntity<Page<DistrictDto>> getByRegion(@RequestParam Integer regionId, Pageable pageable) {
        return ResponseEntity.ok(districtService.findAllByRegionId(regionId, pageable));
    }
}