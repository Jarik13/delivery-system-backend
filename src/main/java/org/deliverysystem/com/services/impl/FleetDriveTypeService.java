package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.FleetDriveTypeDto;
import org.deliverysystem.com.entities.FleetDriveType;
import org.deliverysystem.com.mappers.FleetDriveTypeMapper;
import org.deliverysystem.com.repositories.FleetDriveTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FleetDriveTypeService extends AbstractBaseService<FleetDriveType, FleetDriveTypeDto, Integer> {
    public FleetDriveTypeService(FleetDriveTypeMapper mapper, FleetDriveTypeRepository repository) {
        super(mapper, repository);
    }
}