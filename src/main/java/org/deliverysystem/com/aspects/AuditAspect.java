package org.deliverysystem.com.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.deliverysystem.com.annotations.Auditable;
import org.deliverysystem.com.entities.AuditLog;
import org.deliverysystem.com.repositories.AuditLogRepository;
import org.deliverysystem.com.services.impl.AuditWebSocketService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogRepository auditLogRepository;
    private final AuditWebSocketService auditWebSocketService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        String performedBy = getCurrentUserEmail();
        String details = buildDetails(pjp.getArgs());
        String target = extractTarget(pjp.getArgs(), auditable.targetArgField());

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(auditable.action());
        auditLog.setPerformedBy(performedBy);
        auditLog.setTarget(target);
        auditLog.setDetails(details);
        auditLog.setPerformedAt(LocalDateTime.now());

        try {
            Object result = pjp.proceed();
            auditLog.setStatus("SUCCESS");
            saveAndBroadcast(auditLog);
            return result;
        } catch (Throwable ex) {
            auditLog.setStatus("FAILURE");
            auditLog.setErrorMessage(ex.getMessage());
            saveAndBroadcast(auditLog);
            throw ex;
        }
    }

    private void saveAndBroadcast(AuditLog auditLog) {
        try {
            AuditLog saved = auditLogRepository.save(auditLog);
            auditWebSocketService.broadcast(saved);
        } catch (Exception e) {
            log.error("Failed to save/broadcast audit log: {}", e.getMessage());
        }
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "system";
        return auth.getName();
    }

    private String buildDetails(Object[] args) {
        if (args == null || args.length == 0) return "{}";
        try {
            return objectMapper.writeValueAsString(args[0]);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String extractTarget(Object[] args, String fieldName) {
        if (args == null || args.length == 0 || fieldName.isBlank()) return null;
        try {
            Object arg = args[0];
            if (arg instanceof String) return (String) arg;
            Field field = arg.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(arg);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}