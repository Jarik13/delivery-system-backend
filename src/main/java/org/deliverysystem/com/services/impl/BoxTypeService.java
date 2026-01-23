package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.BoxTypeDto;
import org.deliverysystem.com.entities.BoxType;
import org.deliverysystem.com.mappers.BoxTypeMapper;
import org.deliverysystem.com.repositories.BoxTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class BoxTypeService extends AbstractBaseService<BoxType, BoxTypeDto, Integer> {
    public BoxTypeService(BoxTypeMapper mapper, BoxTypeRepository repository) {
        super(mapper, repository);
    }
}