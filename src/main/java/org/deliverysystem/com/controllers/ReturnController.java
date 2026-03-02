package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.services.impl.ReturnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/returns")
@Tag(name = "Returns", description = "Повернення")
public class ReturnController extends AbstractBaseController<ReturnDto, Integer> {
    private final ReturnService returnService;

    public ReturnController(ReturnService service) {
        super(service);
        this.returnService = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ReturnDto> findByTracking(@RequestParam String trackingNumber) {
        return ResponseEntity.ok(returnService.findByTrackingNumber(trackingNumber));
    }
}
