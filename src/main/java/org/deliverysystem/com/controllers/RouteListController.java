package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.services.impl.RouteListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/route-lists")
@Tag(name = "Route Lists", description = "Маршрутні листи кур'єрів")
public class RouteListController extends AbstractBaseController<RouteListDto, Integer> {
    private final RouteListService routeListService;

    public RouteListController(RouteListService service) {
        super(service);
        this.routeListService = service;
    }

    @Operation(summary = "Отримати всі листи конкретного кур'єра")
    @GetMapping(params = "courierId")
    public ResponseEntity<Page<RouteListDto>> getByCourier(@RequestParam Integer courierId, Pageable pageable) {
        return ResponseEntity.ok(routeListService.findAllByCourierId(courierId, pageable));
    }
}