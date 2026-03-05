package org.deliverysystem.com.services.strategy;

import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.enums.Role;

public interface UserPersistenceStrategy {
    Role getSupportedRole();
    void save(CreateUserDto dto, String keycloakId);
    void delete(String keycloakId);
    UserDbDataDto findByKeycloakId(String keycloakId);
}