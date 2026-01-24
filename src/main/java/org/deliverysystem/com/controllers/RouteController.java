package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.services.impl.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "Routes", description = "Шаблони магістральних маршрутів")
public class RouteController extends AbstractBaseController<RouteDto, Integer> {
    private final RouteService routeService;

    public RouteController(RouteService service) {
        super(service);
        this.routeService = service;
    }

    @Operation(summary = "Знайти маршрути, що виїжджають з відділення")
    @GetMapping("branchId")
    public ResponseEntity<Page<RouteDto>> getByOriginBranch(@RequestParam Integer branchId, Pageable pageable) {
        return ResponseEntity.ok(routeService.findAllByOriginBranchId(branchId, pageable));
    }
}