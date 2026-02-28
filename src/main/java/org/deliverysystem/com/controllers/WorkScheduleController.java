package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.WorkScheduleDto;
import org.deliverysystem.com.services.impl.WorkScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/work-schedules")
@Tag(name = "Work Schedules", description = "Графіки роботи")
@RequiredArgsConstructor
public class WorkScheduleController {
    private final WorkScheduleService workScheduleService;

    @Operation(summary = "Отримати графік роботи відділення")
    @GetMapping(params = "branchId")
    public ResponseEntity<Page<WorkScheduleDto>> getByBranch(@RequestParam Integer branchId, Pageable pageable) {
        return ResponseEntity.ok(workScheduleService.findAllByBranchId(branchId, pageable));
    }
}