package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ShipmentStatusDto;
import org.deliverysystem.com.services.impl.ShipmentStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipment-statuses")
@Tag(name = "Shipment Statuses", description = "Статуси вантажу")
public class ShipmentStatusController extends AbstractBaseController<ShipmentStatusDto, Integer> {
    public ShipmentStatusController(ShipmentStatusService service) {
        super(service);
    }
}