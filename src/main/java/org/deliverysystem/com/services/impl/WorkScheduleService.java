package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.WorkScheduleDto;
import org.deliverysystem.com.entities.WorkSchedule;
import org.deliverysystem.com.mappers.WorkScheduleMapper;
import org.deliverysystem.com.repositories.WorkScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkScheduleService extends AbstractBaseService<WorkSchedule, WorkScheduleDto, Integer> {
    public WorkScheduleService(WorkScheduleRepository repository, WorkScheduleMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<WorkScheduleDto> findAllByBranchId(Integer branchId, Pageable pageable) {
        return ((WorkScheduleRepository) repository).findAllByBranchId(branchId, pageable).map(mapper::toDto);
    }
}