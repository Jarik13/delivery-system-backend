package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.BoxVariantDto;
import org.deliverysystem.com.entities.BoxVariant;
import org.deliverysystem.com.mappers.BoxVariantMapper;
import org.deliverysystem.com.repositories.BoxVariantRepository;
import org.springframework.stereotype.Service;

@Service
public class BoxVariantService extends AbstractBaseService<BoxVariant, BoxVariantDto, Integer> {
    public BoxVariantService(BoxVariantRepository repository, BoxVariantMapper mapper) {
        super(mapper, repository);
    }
}