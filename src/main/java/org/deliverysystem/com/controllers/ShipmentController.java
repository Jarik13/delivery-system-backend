package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.services.impl.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipments")
@Tag(name = "Shipments", description = "Відправлення")
public class ShipmentController extends AbstractBaseController<ShipmentDto, Integer> {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService service) {
        super(service);
        this.shipmentService = service;
    }

    @Operation(summary = "Знайти за трек-номером")
    @GetMapping("/tracking/{tracking}")
    public ResponseEntity<ShipmentDto> findByTracking(@PathVariable String tracking) {
        return ResponseEntity.ok(shipmentService.findByTrackingNumber(tracking));
    }

    @Operation(summary = "Історія відправлень клієнта")
    @GetMapping("/by-sender/{senderId}")
    public ResponseEntity<Page<ShipmentDto>> getBySender(@PathVariable Integer senderId, Pageable pageable) {
        return ResponseEntity.ok(shipmentService.findAllBySenderId(senderId, pageable));
    }
}
