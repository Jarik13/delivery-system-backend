package org.deliverysystem.com.services;

import org.deliverysystem.com.dtos.audit.AuditLogDto;
import org.deliverysystem.com.dtos.search.AuditLogSearchCriteria;
import org.deliverysystem.com.utils.RestPage;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    RestPage<AuditLogDto> findAll(AuditLogSearchCriteria criteria, Pageable pageable);
}