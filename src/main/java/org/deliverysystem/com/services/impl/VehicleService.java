package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.VehicleDto;
import org.deliverysystem.com.entities.Vehicle;
import org.deliverysystem.com.mappers.VehicleMapper;
import org.deliverysystem.com.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService extends AbstractBaseService<Vehicle, VehicleDto, Integer> {
    public VehicleService(VehicleRepository repository, VehicleMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public VehicleDto findByLicensePlate(String licensePlate) {
        return ((VehicleRepository) repository).findByLicensePlate(licensePlate).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND_BY_PLATE + licensePlate));
    }
}