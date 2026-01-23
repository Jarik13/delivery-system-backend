package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.TripDto;
import org.deliverysystem.com.services.impl.TripService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips")
@Tag(name = "Trips", description = "Управління магістральними рейсами")
public class TripController extends AbstractBaseController<TripDto, Integer> {
    public TripController(TripService service) {
        super(service);
    }
}