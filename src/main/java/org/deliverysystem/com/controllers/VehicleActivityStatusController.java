package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.VehicleActivityStatusDto;
import org.deliverysystem.com.services.impl.VehicleActivityStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicle-activity-statuses")
@Tag(name = "Vehicle Activity Statuses", description = "Експлуатаційні стани авто")
public class VehicleActivityStatusController extends AbstractBaseController<VehicleActivityStatusDto, Integer> {
    public VehicleActivityStatusController(VehicleActivityStatusService service) {
        super(service);
    }
}