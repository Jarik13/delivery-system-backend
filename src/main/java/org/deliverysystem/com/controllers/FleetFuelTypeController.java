package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.FleetFuelTypeDto;
import org.deliverysystem.com.services.impl.FleetFuelTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet-fuel-types")
@Tag(name = "Fleet Fuel Types", description = "Типи пального")
public class FleetFuelTypeController extends AbstractBaseController<FleetFuelTypeDto, Integer> {
    public FleetFuelTypeController(FleetFuelTypeService service) {
        super(service);
    }
}