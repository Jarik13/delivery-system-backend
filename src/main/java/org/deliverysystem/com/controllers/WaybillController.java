package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.deliverysystem.com.dtos.search.TripSearchCriteria;
import org.deliverysystem.com.dtos.search.WaybillSearchCriteria;
import org.deliverysystem.com.dtos.waybills.CreateWaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.export.WaybillExportContext;
import org.deliverysystem.com.services.impl.WaybillService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/waybills")
@Tag(name = "Waybills", description = "Транспортні накладні")
public class WaybillController {
    private final WaybillService waybillService;
    private final WaybillExportContext exportContext;

    public WaybillController(WaybillService waybillService,
                             WaybillExportContext exportContext) {
        this.waybillService = waybillService;
        this.exportContext = exportContext;
    }

    @Operation(summary = "Отримати всі накладні (пагінація)")
    @GetMapping
    public ResponseEntity<RestPage<WaybillDto>> getAll(
            @ParameterObject WaybillSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(waybillService.findAll(criteria, pageable));
    }

    @Operation(summary = "Отримати накладну за ID")
    @GetMapping("/{id}")
    public ResponseEntity<WaybillDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(waybillService.findById(id));
    }

    @Operation(summary = "Знайти накладну за номером документу")
    @GetMapping("/search")
    public ResponseEntity<WaybillDto> findByNumber(@RequestParam Integer number) {
        return ResponseEntity.ok(waybillService.findByNumber(number));
    }

    @Operation(summary = "Створити накладну")
    @PostMapping
    public ResponseEntity<WaybillDto> create(@Valid @RequestBody CreateWaybillDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(waybillService.create(dto));
    }

    @Operation(summary = "Видалити накладну")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        waybillService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Експорт накладних",
            description = "Формати: csv | xlsx | pdf | json")
    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @Parameter(description = "Формат: csv | xlsx | pdf | json")
            @RequestParam String format, @Parameter(description = "Фільтр за номером")
            @RequestParam(required = false) Integer number, @Parameter(description = "Список конкретних ID")
            @RequestParam(required = false) List<Integer> ids) {
        List<WaybillDto> waybills = (ids != null && !ids.isEmpty())
                ? waybillService.findAllByIds(ids)
                : waybillService.findAllForExport(number);

        return exportContext.export(format, waybills);
    }

    @Operation(summary = "Список підтримуваних форматів експорту")
    @GetMapping("/export/formats")
    public ResponseEntity<List<String>> getSupportedFormats() {
        return ResponseEntity.ok(exportContext.getSupportedFormats());
    }
}