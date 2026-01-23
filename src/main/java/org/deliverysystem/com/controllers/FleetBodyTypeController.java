package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.FleetBodyTypeDto;
import org.deliverysystem.com.services.impl.FleetBodyTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet-body-types")
@Tag(name = "Fleet Body Types", description = "Типи кузовів")
public class FleetBodyTypeController extends AbstractBaseController<FleetBodyTypeDto, Integer> {
    public FleetBodyTypeController(FleetBodyTypeService service) {
        super(service);
    }
}