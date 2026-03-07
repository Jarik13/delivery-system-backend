package org.deliverysystem.com.services.strategy;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.Driver;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.DriverRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverPersistenceStrategy implements UserPersistenceStrategy {
    private final DriverRepository driverRepository;

    @Override public Role getSupportedRole() { return Role.DRIVER; }

    @Override
    public void save(CreateUserDto dto, String keycloakId) {
        Driver d = new Driver();
        d.setKeycloakId(keycloakId);
        d.setFirstName(dto.firstName());
        d.setLastName(dto.lastName());
        d.setMiddleName(dto.middleName());
        d.setPhoneNumber(dto.phoneNumber());
        driverRepository.save(d);
    }

    @Override
    public void delete(String keycloakId) {
        driverRepository.findByKeycloakId(keycloakId).ifPresent(driverRepository::delete);
    }

    @Override
    public Optional<UserDbDataDto> findByKeycloakId(String keycloakId) {
        return driverRepository.findByKeycloakId(keycloakId)
                .map(d -> new UserDbDataDto(d.getId(), d.getMiddleName(), d.getPhoneNumber()));
    }
}