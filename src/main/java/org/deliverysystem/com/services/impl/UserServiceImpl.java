package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.annotations.Auditable;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.services.BranchAssignable;
import org.deliverysystem.com.services.UserService;
import org.deliverysystem.com.services.strategy.UserPersistenceStrategy;
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
    @Auditable(action = "CREATE_USER", targetArgField = "email")
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
                dto.role().name(), false, dto.branchId()
        );
    }

    @Override
    @Auditable(action = "DELETE_USER")
    @Transactional
    public void deleteUser(String keycloakId) {
        keycloakAdminService.deleteUser(keycloakId);

        for (Role role : Role.values()) {
            strategyRegistry.getStrategy(role).delete(keycloakId);
        }
        log.info("Deleted user keycloakId={}", keycloakId);
    }

    @Override
    @Auditable(action = "RESEND_EMAIL")
    public void resendEmail(String keycloakId) {
        keycloakAdminService.resendVerificationEmail(keycloakId);
        log.info("Verification email resent: keycloakId={}", keycloakId);
    }

    @Override
    @Auditable(action = "UPDATE_ROLE")
    public void updateRole(String keycloakId, String newRole) {
        keycloakAdminService.updateUserRole(keycloakId, Role.valueOf(newRole));
        log.info("Role updated: keycloakId={}, role={}", keycloakId, newRole);
    }

    @Override
    @Auditable(action = "UPDATE_BRANCH")
    @Transactional
    public void updateBranch(String keycloakId, Integer branchId) {
        UserPersistenceStrategy strategy = strategyRegistry.getStrategy(Role.EMPLOYEE);

        if (!(strategy instanceof BranchAssignable branchAssignable)) {
            throw new UnsupportedOperationException("EMPLOYEE strategy does not support branch assignment");
        }

        branchAssignable.updateBranch(keycloakId, branchId);
        log.info("Branch updated: keycloakId={}, branchId={}", keycloakId, branchId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return keycloakAdminService.getAllUsers();
    }
}