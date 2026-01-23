package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.services.impl.BranchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@Tag(name = "Branches", description = "Управління відділеннями")
public class BranchController extends AbstractBaseController<BranchDto, Integer> {
    private final BranchService branchService;

    public BranchController(BranchService service) {
        super(service);
        this.branchService = service;
    }

    @Operation(summary = "Знайти відділення за містом")
    @GetMapping("/by-city/{cityId}")
    public ResponseEntity<Page<BranchDto>> getByCity(@PathVariable Integer cityId, Pageable pageable) {
        return ResponseEntity.ok(branchService.findAllByCityId(cityId, pageable));
    }
}