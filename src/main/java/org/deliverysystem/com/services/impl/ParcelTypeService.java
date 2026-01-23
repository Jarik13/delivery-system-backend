package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.ParcelTypeDto;
import org.deliverysystem.com.entities.ParcelType;
import org.deliverysystem.com.mappers.ParcelTypeMapper;
import org.deliverysystem.com.repositories.ParcelTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ParcelTypeService extends AbstractBaseService<ParcelType, ParcelTypeDto, Integer> {
    public ParcelTypeService(ParcelTypeMapper mapper, ParcelTypeRepository repository) {
        super(mapper, repository);
    }
}