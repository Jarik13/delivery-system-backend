package org.deliverysystem.com.services.strategy;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UpdateProfileDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.entities.Courier;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.repositories.CourierRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CourierPersistenceStrategy implements UserPersistenceStrategy {
    private final CourierRepository courierRepository;

    @Override
    public Role getSupportedRole() {
        return Role.COURIER;
    }

    @Override
    public void save(CreateUserDto dto, String keycloakId) {
        Courier c = new Courier();
        c.setKeycloakId(keycloakId);
        c.setFirstName(dto.firstName());
        c.setLastName(dto.lastName());
        c.setMiddleName(dto.middleName());
        c.setPhoneNumber(dto.phoneNumber());
        courierRepository.save(c);
    }

    @Override
    public void delete(String keycloakId) {
        courierRepository.findByKeycloakId(keycloakId).ifPresent(courierRepository::delete);
    }

    @Override
    public void updateProfile(String keycloakId, UpdateProfileDto dto) {
        courierRepository.findByKeycloakId(keycloakId).ifPresent(c -> {
            applyCommonFields(c, dto);
            courierRepository.save(c);
        });
    }

    @Override
    public Optional<UserDbDataDto> findByKeycloakId(String keycloakId) {
        return courierRepository.findByKeycloakId(keycloakId)
                .map(c -> UserDbDataDto.of(c.getId(), c.getMiddleName(), c.getPhoneNumber()));
    }
}