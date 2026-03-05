package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.services.UserService;
import org.deliverysystem.com.services.strategy.UserPersistenceStrategyRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final KeycloakAdminService keycloakAdminService;
    private final UserPersistenceStrategyRegistry strategyRegistry;

    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        String keycloakId = keycloakAdminService.createUser(dto);
        log.info("Keycloak: created user {} with role {}", dto.email(), dto.role());

        strategyRegistry.getStrategy(dto.role()).save(dto, keycloakId);
        log.info("DB: saved keycloakId={}", keycloakId);

        return new UserDto(
                keycloakId, dto.email(),
                dto.firstName(), dto.lastName(),
                dto.middleName(), dto.phoneNumber(),
                dto.role().name(), false
        );
    }

    @Override
    @Transactional
    public void deleteUser(String keycloakId) {
        keycloakAdminService.deleteUser(keycloakId);

        for (Role role : Role.values()) {
            strategyRegistry.getStrategy(role).delete(keycloakId);
        }
        log.info("Deleted user keycloakId={}", keycloakId);
    }

    @Override
    public void resendEmail(String keycloakId) {
        keycloakAdminService.resendVerificationEmail(keycloakId);
        log.info("Verification email resent: keycloakId={}", keycloakId);
    }

    @Override
    public void updateRole(String keycloakId, String newRole) {
        keycloakAdminService.updateUserRole(keycloakId, Role.valueOf(newRole));
        log.info("Role updated: keycloakId={}, role={}", keycloakId, newRole);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return keycloakAdminService.getAllUsers();
    }
}