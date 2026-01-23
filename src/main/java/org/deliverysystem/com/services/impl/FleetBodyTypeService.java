package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.FleetBodyTypeDto;
import org.deliverysystem.com.entities.FleetBodyType;
import org.deliverysystem.com.mappers.FleetBodyTypeMapper;
import org.deliverysystem.com.repositories.FleetBodyTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class FleetBodyTypeService extends AbstractBaseService<FleetBodyType, FleetBodyTypeDto, Integer> {
    public FleetBodyTypeService(FleetBodyTypeMapper mapper, FleetBodyTypeRepository repository) {
        super(mapper, repository);
    }
}