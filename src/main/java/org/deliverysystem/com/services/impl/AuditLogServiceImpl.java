package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.audit.AuditLogDto;
import org.deliverysystem.com.dtos.search.AuditLogSearchCriteria;
import org.deliverysystem.com.entities.AuditLog;
import org.deliverysystem.com.repositories.AuditLogRepository;
import org.deliverysystem.com.services.AuditLogService;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public RestPage<AuditLogDto> findAll(AuditLogSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return new RestPage<>(auditLogRepository.findAll(pageable).map(AuditLogDto::from));
        }

        Specification<AuditLog> spec = Specification
                .where(SpecificationUtils.<AuditLog>in("action", criteria.actions()))
                .and(SpecificationUtils.iLike("performedBy", criteria.performedBy()))
                .and(SpecificationUtils.iLike("target", criteria.target()))
                .and(SpecificationUtils.<AuditLog>in("status", criteria.statuses()))
                .and(SpecificationUtils.gte("performedAt", criteria.performedAtFrom()))
                .and(SpecificationUtils.lte("performedAt", criteria.performedAtTo()));

        return new RestPage<>(auditLogRepository.findAll(spec, pageable).map(AuditLogDto::from));
    }
}