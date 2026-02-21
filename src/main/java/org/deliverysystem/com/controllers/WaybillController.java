package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.export.WaybillExportContext;
import org.deliverysystem.com.services.impl.WaybillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/waybills")
@Tag(name = "Waybills", description = "Транспортні накладні")
public class WaybillController extends AbstractBaseController<WaybillDto, Integer> {
    private final WaybillService waybillService;
    private final WaybillExportContext exportContext;

    public WaybillController(WaybillService service, WaybillExportContext exportContext) {
        super(service);
        this.waybillService = service;
        this.exportContext = exportContext;
    }

    @Operation(summary = "Знайти накладну за номером")
    @GetMapping("/search")
    public ResponseEntity<WaybillDto> findByNumber(@RequestParam Integer number) {
        return ResponseEntity.ok(waybillService.findByNumber(number));
    }

    @Operation(summary = "Експорт накладних", description = "Підтримувані формати: csv, xlsx, pdf, json")
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @Parameter(description = "Формат: csv | xlsx | pdf | json") @RequestParam String format,
            @Parameter(description = "Фільтр за номером накладної") @RequestParam(required = false) Integer number) {
        List<WaybillDto> waybills = waybillService.findAllForExport(number);
        return exportContext.export(format, waybills);
    }

    @Operation(summary = "Список підтримуваних форматів експорту")
    @GetMapping("/export/formats")
    public ResponseEntity<List<String>> getSupportedFormats() {
        return ResponseEntity.ok(exportContext.getSupportedFormats());
    }
}