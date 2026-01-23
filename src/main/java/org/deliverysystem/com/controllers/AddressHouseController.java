package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.AddressHouseDto;
import org.deliverysystem.com.services.impl.AddressHouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address-houses")
@Tag(name = "Address Houses", description = "Довідник будинків")
public class AddressHouseController extends AbstractBaseController<AddressHouseDto, Integer> {
    private final AddressHouseService addressHouseService;

    public AddressHouseController(AddressHouseService service) {
        super(service);
        this.addressHouseService = service;
    }

    @Operation(summary = "Отримати будинки на вулиці")
    @GetMapping("/by-street/{streetId}")
    public ResponseEntity<Page<AddressHouseDto>> getByStreet(@PathVariable Integer streetId, Pageable pageable) {
        return ResponseEntity.ok(addressHouseService.findAllByStreetId(streetId, pageable));
    }
}