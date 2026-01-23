package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.FleetDriveTypeDto;
import org.deliverysystem.com.services.impl.FleetDriveTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fleet-drive-types")
@Tag(name = "Fleet Drive Types", description = "Типи приводу")
public class FleetDriveTypeController extends AbstractBaseController<FleetDriveTypeDto, Integer> {
    public FleetDriveTypeController(FleetDriveTypeService service) {
        super(service);
    }
}