package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.audit.AuditLogDto;
import org.deliverysystem.com.dtos.search.AuditLogSearchCriteria;
import org.deliverysystem.com.services.AuditLogService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "Audit Logs", description = "Audit log viewer (SUPER_ADMIN only)")
public class AuditLogController {
    private final AuditLogService auditLogService;

    @Operation(summary = "Get audit logs with filtering and pagination")
    @GetMapping
    public ResponseEntity<RestPage<AuditLogDto>> getAll(
            @ParameterObject AuditLogSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(auditLogService.findAll(criteria, pageable));
    }
}