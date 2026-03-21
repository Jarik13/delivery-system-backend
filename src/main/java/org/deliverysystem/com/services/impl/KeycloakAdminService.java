package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.services.strategy.UserPersistenceStrategyRegistry;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.keycloak.OAuth2Constants;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final UserPersistenceStrategyRegistry strategyRegistry;

    private static final List<String> SYSTEM_ROLES = Arrays.stream(Role.values())
            .map(Role::name)
            .toList();

    private Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public String createUser(CreateUserDto dto) {
        RealmResource realmResource = getKeycloak().realm(realm);

        List<UserRepresentation> existing = realmResource.users().searchByEmail(dto.email(), true);
        if (!existing.isEmpty())
            throw new IllegalArgumentException("Користувач з таким email вже існує");

        UserRepresentation user = new UserRepresentation();
        user.setEmail(dto.email());
        user.setUsername(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        Response response = realmResource.users().create(user);
        String keycloakId = CreatedResponseUtil.getCreatedId(response);

        RoleRepresentation roleRep = realmResource.roles()
                .get(dto.role().name())
                .toRepresentation();
        realmResource.users().get(keycloakId).roles().realmLevel().add(List.of(roleRep));

        realmResource.users().get(keycloakId).executeActionsEmail(List.of("UPDATE_PASSWORD", "VERIFY_EMAIL"));

        return keycloakId;
    }

    public List<UserDto> getAllUsers() {
        RealmResource realmResource = getKeycloak().realm(realm);

        return realmResource.users().list().stream()
                .filter(u -> u.getUsername() != null && !u.getUsername().startsWith("service-account"))
                .map(u -> {
                    String roleName = realmResource.users().get(u.getId())
                            .roles().realmLevel().listEffective().stream()
                            .map(RoleRepresentation::getName)
                            .filter(SYSTEM_ROLES::contains)
                            .findFirst()
                            .orElse("NONE");

                    String middleName = null;
                    String phoneNumber = null;
                    Integer branchId = null;

                    if (!roleName.equals("NONE")) {
                        Role role = Role.valueOf(roleName);
                        var dbData = strategyRegistry.getStrategy(role).findByKeycloakId(u.getId()).orElse(null);
                        if (dbData != null) {
                            middleName = dbData.middleName();
                            phoneNumber = dbData.phoneNumber();
                            branchId = dbData.branchId();
                        }
                    }

                    return new UserDto(
                            u.getId(),
                            u.getEmail(),
                            u.getFirstName(),
                            u.getLastName(),
                            middleName,
                            phoneNumber,
                            roleName,
                            Boolean.TRUE.equals(u.isEmailVerified()),
                            branchId,
                            null
                    );
                })
                .toList();
    }

    public void deleteUser(String keycloakId) {
        getKeycloak().realm(realm).users().delete(keycloakId);
    }

    public void resendVerificationEmail(String keycloakId) {
        getKeycloak().realm(realm).users().get(keycloakId)
                .executeActionsEmail(List.of("UPDATE_PASSWORD", "VERIFY_EMAIL"));
    }

    public void updateUserRole(String keycloakId, Role newRole) {
        RealmResource realmResource = getKeycloak().realm(realm);

        List<RoleRepresentation> currentRoles = realmResource.users().get(keycloakId)
                .roles().realmLevel().listEffective().stream()
                .filter(r -> SYSTEM_ROLES.contains(r.getName()))
                .toList();

        if (!currentRoles.isEmpty())
            realmResource.users().get(keycloakId).roles().realmLevel().remove(currentRoles);

        RoleRepresentation roleRep = realmResource.roles()
                .get(newRole.name())
                .toRepresentation();
        realmResource.users().get(keycloakId).roles().realmLevel().add(List.of(roleRep));
    }

    public UserDto getUserByKeycloakId(String keycloakId) {
        RealmResource realmResource = getKeycloak().realm(realm);
        UserRepresentation u = realmResource.users().get(keycloakId).toRepresentation();

        String roleName = realmResource.users().get(keycloakId)
                .roles().realmLevel().listEffective().stream()
                .map(RoleRepresentation::getName)
                .filter(SYSTEM_ROLES::contains)
                .findFirst()
                .orElse("NONE");

        String middleName = null, phoneNumber = null, licenseNumber = null;
        Integer branchId = null;

        if (!roleName.equals("NONE")) {
            var dbData = strategyRegistry.getStrategy(Role.valueOf(roleName)).findByKeycloakId(keycloakId).orElse(null);
            if (dbData != null) {
                middleName = dbData.middleName();
                phoneNumber = dbData.phoneNumber();
                branchId = dbData.branchId();
                licenseNumber = dbData.licenseNumber();
            }
        }

        return new UserDto(u.getId(), u.getEmail(), u.getFirstName(), u.getLastName(),
                middleName, phoneNumber, roleName, Boolean.TRUE.equals(u.isEmailVerified()),
                branchId, licenseNumber);
    }

    public void updateUserNames(String keycloakId, String firstName, String lastName) {
        RealmResource realmResource = getKeycloak().realm(realm);
        UserRepresentation u = realmResource.users().get(keycloakId).toRepresentation();
        if (firstName != null) u.setFirstName(firstName);
        if (lastName != null) u.setLastName(lastName);
        realmResource.users().get(keycloakId).update(u);
    }
}