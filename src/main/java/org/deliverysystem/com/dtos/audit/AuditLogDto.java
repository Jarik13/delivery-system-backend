package org.deliverysystem.com.dtos.audit;

import org.deliverysystem.com.entities.AuditLog;

import java.time.LocalDateTime;

public record AuditLogDto(
        Long id,
        String action,
        String performedBy,
        String target,
        String details,
        String status,
        String errorMessage,
        LocalDateTime performedAt
) {
    public static AuditLogDto from(AuditLog log) {
        return new AuditLogDto(
                log.getId(),
                log.getAction(),
                log.getPerformedBy(),
                log.getTarget(),
                log.getDetails(),
                log.getStatus(),
                log.getErrorMessage(),
                log.getPerformedAt()
        );
    }
}