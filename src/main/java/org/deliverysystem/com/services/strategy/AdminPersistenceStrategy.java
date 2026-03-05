package org.deliverysystem.com.services.strategy;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.Admin;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.AdminRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminPersistenceStrategy implements UserPersistenceStrategy {
    private final AdminRepository adminRepository;

    @Override
    public Role getSupportedRole() {
        return Role.ADMIN;
    }

    @Override
    public void save(CreateUserDto dto, String keycloakId) {
        Admin a = new Admin();
        a.setKeycloakId(keycloakId);
        a.setFirstName(dto.firstName());
        a.setLastName(dto.lastName());
        a.setMiddleName(dto.middleName());
        a.setPhoneNumber(dto.phoneNumber());
        adminRepository.save(a);
    }

    @Override
    public void delete(String keycloakId) {
        adminRepository.findByKeycloakId(keycloakId).ifPresent(adminRepository::delete);
    }

    @Override
    public UserDbDataDto findByKeycloakId(String keycloakId) {
        return adminRepository.findByKeycloakId(keycloakId)
                .map(a -> new UserDbDataDto(a.getMiddleName(), a.getPhoneNumber()))
                .orElse(null);
    }
}