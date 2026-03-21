package org.deliverysystem.com.services.strategy;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UpdateProfileDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.Employee;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.deliverysystem.com.services.BranchAssignable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeePersistenceStrategy implements UserPersistenceStrategy, BranchAssignable {
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Role getSupportedRole() {
        return Role.EMPLOYEE;
    }

    @Override
    public void save(CreateUserDto dto, String keycloakId) {
        Employee e = new Employee();
        e.setKeycloakId(keycloakId);
        e.setFirstName(dto.firstName());
        e.setLastName(dto.lastName());
        e.setMiddleName(dto.middleName());
        e.setPhoneNumber(dto.phoneNumber());
        e.setBranch(branchRepository.findById(dto.branchId())
                .orElseThrow(() -> new EntityNotFoundException("Відділення з id=" + dto.branchId() + " не знайдено")));
        employeeRepository.save(e);
    }

    @Override
    public void delete(String keycloakId) {
        employeeRepository.findByKeycloakId(keycloakId).ifPresent(employeeRepository::delete);
    }

    @Override
    public void updateProfile(String keycloakId, UpdateProfileDto dto) {
        employeeRepository.findByKeycloakId(keycloakId).ifPresent(e -> {
            applyCommonFields(e, dto);
            employeeRepository.save(e);
        });
    }

    @Override
    public Optional<UserDbDataDto> findByKeycloakId(String keycloakId) {
        return employeeRepository.findByKeycloakId(keycloakId)
                .map(e -> UserDbDataDto.ofEmployee(
                        e.getId(),
                        e.getMiddleName(),
                        e.getPhoneNumber(),
                        e.getBranch() != null ? e.getBranch().getId() : null
                ));
    }

    @Override
    public void updateBranch(String keycloakId, Integer branchId) {
        employeeRepository.findByKeycloakId(keycloakId)
                .ifPresent(employee -> {
                    employee.setBranch(branchRepository.getReferenceById(branchId));
                    employeeRepository.save(employee);
                });
    }
}