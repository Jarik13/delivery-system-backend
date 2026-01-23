package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.FleetTransmissionTypeDto;
import org.deliverysystem.com.services.impl.FleetTransmissionTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet-transmission-types")
@Tag(name = "Fleet Transmission Types", description = "Типи КПП")
public class FleetTransmissionTypeController extends AbstractBaseController<FleetTransmissionTypeDto, Integer> {
    public FleetTransmissionTypeController(FleetTransmissionTypeService service) {
        super(service);
    }
}