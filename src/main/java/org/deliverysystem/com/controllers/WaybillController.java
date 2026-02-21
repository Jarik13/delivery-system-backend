package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.services.impl.WaybillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waybills")
@Tag(name = "Waybills", description = "Транспортні накладні")
public class WaybillController extends AbstractBaseController<WaybillDto, Integer> {
    private final WaybillService waybillService;

    public WaybillController(WaybillService service) {
        super(service);
        this.waybillService = service;
    }

    @Operation(summary = "Знайти накладну за номером")
    @GetMapping("/search")
    public ResponseEntity<WaybillDto> findByNumber(@RequestParam Integer number) {
        return ResponseEntity.ok(waybillService.findByNumber(number));
    }
}