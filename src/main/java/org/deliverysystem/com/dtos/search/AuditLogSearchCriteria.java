package org.deliverysystem.com.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Search criteria for audit logs filtering")
public record AuditLogSearchCriteria(

        @Schema(description = "Filter by action type", example = "CREATE_USER")
        List<String> actions,

        @Schema(description = "Filter by performer email (partial match)", example = "admin@gmail.com")
        String performedBy,

        @Schema(description = "Filter by target (partial match)", example = "user@gmail.com")
        String target,

        @Schema(description = "Filter by status", example = "SUCCESS")
        List<String> statuses,

        @Schema(description = "Performed at from")
        LocalDateTime performedAtFrom,

        @Schema(description = "Performed at to")
        LocalDateTime performedAtTo
) {}