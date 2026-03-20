package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.dtos.employees.EmployeeDto;
import org.deliverysystem.com.dtos.employees.EmployeeProfileDto;
import org.deliverysystem.com.entities.Branch;
import org.deliverysystem.com.entities.Employee;
import org.deliverysystem.com.mappers.EmployeeMapper;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends AbstractBaseService<Employee, EmployeeDto, Integer> {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        super(mapper, repository);
        this.employeeRepository = repository;
    }

    public EmployeeProfileDto getProfile(Integer employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        Branch branch = e.getBranch();

        EmployeeProfileDto.BranchInfo branchInfo = null;
        if (branch != null) {
            var dp = branch.getDeliveryPoint();
            String name = dp.getName();
            String address = dp.getAddress() != null ? ", " + dp.getAddress() : "";
            branchInfo = new EmployeeProfileDto.BranchInfo(
                    branch.getId(),
                    dp.getId(),
                    name + address,
                    dp.getAddress()
            );
        }

        return new EmployeeProfileDto(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getMiddleName(),
                e.getPhoneNumber(),
                branchInfo
        );
    }
}