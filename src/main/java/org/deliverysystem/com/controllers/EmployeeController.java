package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.employees.EmployeeDto;
import org.deliverysystem.com.dtos.employees.EmployeeProfileDto;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.services.impl.EmployeeService;
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

    @GetMapping("/me")
    @Operation(summary = "Отримати дані поточного працівника")
    public ResponseEntity<EmployeeProfileDto> getMe(@CurrentUser CurrentUserDto user) {
        return ResponseEntity.ok(employeeService.getProfile(user.id()));
    }
}