package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.dtos.audit.AuditLogDto;
import org.deliverysystem.com.entities.AuditLog;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(AuditLog auditLog) {
        try {
            messagingTemplate.convertAndSend("/topic/audit", AuditLogDto.from(auditLog));
            log.debug("Audit log broadcast: action={}, by={}", auditLog.getAction(), auditLog.getPerformedBy());
        } catch (Exception e) {
            log.error("Failed to broadcast audit log: {}", e.getMessage());
        }
    }
}