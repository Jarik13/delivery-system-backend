package org.deliverysystem.com.services.strategy;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.Employee;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeePersistenceStrategy implements UserPersistenceStrategy {
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
    public UserDbDataDto findByKeycloakId(String keycloakId) {
        return employeeRepository.findByKeycloakId(keycloakId)
                .map(e -> new UserDbDataDto(e.getMiddleName(), e.getPhoneNumber()))
                .orElse(null);
    }
}