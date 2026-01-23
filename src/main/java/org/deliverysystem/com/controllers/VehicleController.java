package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.VehicleDto;
import org.deliverysystem.com.services.impl.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicles", description = "Облік автомобілів")
public class VehicleController extends AbstractBaseController<VehicleDto, Integer> {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService service) {
        super(service);
        this.vehicleService = service;
    }

    @Operation(summary = "Знайти авто за держ. номером")
    @GetMapping("/search")
    public ResponseEntity<VehicleDto> findByLicensePlate(@RequestParam String licensePlate) {
        return ResponseEntity.ok(vehicleService.findByLicensePlate(licensePlate));
    }
}