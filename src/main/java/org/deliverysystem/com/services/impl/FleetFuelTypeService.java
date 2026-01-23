package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.FleetFuelTypeDto;
import org.deliverysystem.com.entities.FleetFuelType;
import org.deliverysystem.com.mappers.FleetFuelTypeMapper;
import org.deliverysystem.com.repositories.FleetFuelTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FleetFuelTypeService extends AbstractBaseService<FleetFuelType, FleetFuelTypeDto, Integer> {
    public FleetFuelTypeService(FleetFuelTypeMapper mapper, FleetFuelTypeRepository repository) {
        super(mapper, repository);
    }
}