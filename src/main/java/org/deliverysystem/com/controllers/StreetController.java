package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.StreetDto;
import org.deliverysystem.com.services.impl.StreetService;
import org.deliverysystem.com.utils.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/streets")
@Tag(name = "Streets", description = "Довідник вулиць")
public class StreetController extends AbstractBaseController<StreetDto, Integer> {
    private final StreetService streetService;

    public StreetController(StreetService service) {
        super(service);
        this.streetService = service;
    }

    @Operation(summary = "Отримати вулиці міста (з пагінацією)")
    @GetMapping(params = "cityId")
    public ResponseEntity<RestPage<StreetDto>> getByCityId(@RequestParam Integer cityId, Pageable pageable) {
        return ResponseEntity.ok(streetService.findAllByCityId(cityId, pageable));
    }
}