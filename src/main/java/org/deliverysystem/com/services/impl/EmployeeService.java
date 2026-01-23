package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.EmployeeDto;
import org.deliverysystem.com.entities.Employee;
import org.deliverysystem.com.mappers.EmployeeMapper;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService extends AbstractBaseService<Employee, EmployeeDto, Integer> {
    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDto> findAllByBranchId(Integer branchId, Pageable pageable) {
        return ((EmployeeRepository) repository).findAllByBranchId(branchId, pageable).map(mapper::toDto);
    }
}