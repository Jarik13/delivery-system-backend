package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.DriverDto;
import org.deliverysystem.com.services.impl.DriverService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers", description = "Водії магістральних перевезень")
public class DriverController extends AbstractBaseController<DriverDto, Integer> {
    public DriverController(DriverService service) {
        super(service);
    }
}