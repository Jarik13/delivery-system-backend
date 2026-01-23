package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ShipmentTypeDto;
import org.deliverysystem.com.services.impl.ShipmentTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipment-types")
@Tag(name = "Shipment Types", description = "Типи відправлень")
public class ShipmentTypeController extends AbstractBaseController<ShipmentTypeDto, Integer> {
    public ShipmentTypeController(ShipmentTypeService service) {
        super(service);
    }
}