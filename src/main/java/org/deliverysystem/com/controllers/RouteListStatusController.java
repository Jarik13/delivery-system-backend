package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.RouteListStatusDto;
import org.deliverysystem.com.services.impl.RouteListStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route-list-statuses")
@Tag(name = "Route List Statuses", description = "Статуси маршрутного листа")
public class RouteListStatusController extends AbstractBaseController<RouteListStatusDto, Integer> {
    public RouteListStatusController(RouteListStatusService service) {
        super(service);
    }
}
