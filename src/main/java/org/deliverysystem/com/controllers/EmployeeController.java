package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.EmployeeDto;
import org.deliverysystem.com.services.impl.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Управління персоналом")
public class EmployeeController extends AbstractBaseController<EmployeeDto, Integer> {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service) {
        super(service);
        this.employeeService = service;
    }

    @Operation(summary = "Отримати всіх співробітників відділення")
    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<Page<EmployeeDto>> getByBranch(@PathVariable Integer branchId, Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAllByBranchId(branchId, pageable));
    }
}