package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.TripStatusDto;
import org.deliverysystem.com.services.impl.TripStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trip-statuses")
@Tag(name = "Trip Statuses", description = "Статуси магістральних рейсів")
public class TripStatusController extends AbstractBaseController<TripStatusDto, Integer> {
    public TripStatusController(TripStatusService service) {
        super(service);
    }
}