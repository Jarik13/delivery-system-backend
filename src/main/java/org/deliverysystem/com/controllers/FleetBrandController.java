package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.FleetBrandDto;
import org.deliverysystem.com.services.impl.FleetBrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet-brands")
@Tag(name = "Fleet Brands", description = "Марки авто")
public class FleetBrandController extends AbstractBaseController<FleetBrandDto, Integer> {
    public FleetBrandController(FleetBrandService service) {
        super(service);
    }
}