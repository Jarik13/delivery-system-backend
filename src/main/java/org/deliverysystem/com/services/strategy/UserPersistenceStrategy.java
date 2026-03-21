package org.deliverysystem.com.services.strategy;

import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UpdateProfileDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.BaseUser;
import org.deliverysystem.com.enums.Role;

import java.util.Optional;

public interface UserPersistenceStrategy {
    Role getSupportedRole();
    void save(CreateUserDto dto, String keycloakId);
    void delete(String keycloakId);
    void updateProfile(String keycloakId, UpdateProfileDto dto);
    Optional<UserDbDataDto> findByKeycloakId(String keycloakId);

    default void applyCommonFields(BaseUser entity, UpdateProfileDto dto) {
        if (dto.firstName() != null) entity.setFirstName(dto.firstName());
        if (dto.lastName() != null) entity.setLastName(dto.lastName());
        if (dto.middleName() != null) entity.setMiddleName(dto.middleName());
        if (dto.phoneNumber() != null) entity.setPhoneNumber(dto.phoneNumber());
    }
}