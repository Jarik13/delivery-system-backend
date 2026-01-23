package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.FleetTransmissionTypeDto;
import org.deliverysystem.com.entities.FleetTransmissionType;
import org.deliverysystem.com.mappers.FleetTransmissionTypeMapper;
import org.deliverysystem.com.repositories.FleetTransmissionTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FleetTransmissionTypeService extends AbstractBaseService<FleetTransmissionType, FleetTransmissionTypeDto, Integer> {
    public FleetTransmissionTypeService(FleetTransmissionTypeMapper mapper, FleetTransmissionTypeRepository repository) {
        super(mapper, repository);
    }
}