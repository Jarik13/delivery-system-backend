package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.ShipmentTypeDto;
import org.deliverysystem.com.entities.ShipmentType;
import org.deliverysystem.com.mappers.ShipmentTypeMapper;
import org.deliverysystem.com.repositories.ShipmentTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentTypeService extends AbstractBaseService<ShipmentType, ShipmentTypeDto, Integer> {
    public ShipmentTypeService(ShipmentTypeMapper mapper, ShipmentTypeRepository repository) {
        super(mapper, repository);
    }
}