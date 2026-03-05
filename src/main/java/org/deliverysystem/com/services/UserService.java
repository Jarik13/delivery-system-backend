package org.deliverysystem.com.services;

import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto dto);
    void deleteUser(String keycloakId);
    void resendEmail(String keycloakId);
    void updateRole(String keycloakId, String newRole);
    List<UserDto> getAllUsers();
}