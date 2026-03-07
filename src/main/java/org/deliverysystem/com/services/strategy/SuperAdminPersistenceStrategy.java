package org.deliverysystem.com.services.strategy;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.SuperAdmin;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.SuperAdminRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SuperAdminPersistenceStrategy implements UserPersistenceStrategy {
    private final SuperAdminRepository superAdminRepository;

    @Override
    public Role getSupportedRole() {
        return Role.SUPER_ADMIN;
    }

    @Override
    public void save(CreateUserDto dto, String keycloakId) {
        SuperAdmin sa = new SuperAdmin();
        sa.setKeycloakId(keycloakId);
        sa.setFirstName(dto.firstName());
        sa.setLastName(dto.lastName());
        sa.setMiddleName(dto.middleName());
        sa.setPhoneNumber(dto.phoneNumber());
        superAdminRepository.save(sa);
    }

    @Override
    public void delete(String keycloakId) {
        superAdminRepository.findByKeycloakId(keycloakId).ifPresent(superAdminRepository::delete);
    }

    @Override
    public Optional<UserDbDataDto> findByKeycloakId(String keycloakId) {
        return superAdminRepository.findByKeycloakId(keycloakId)
                .map(sp -> new UserDbDataDto(sp.getId(), sp.getMiddleName(), sp.getPhoneNumber()));
    }
}