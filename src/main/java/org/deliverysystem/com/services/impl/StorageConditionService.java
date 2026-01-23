package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.StorageConditionDto;
import org.deliverysystem.com.entities.StorageCondition;
import org.deliverysystem.com.mappers.StorageConditionMapper;
import org.deliverysystem.com.repositories.StorageConditionRepository;
import org.springframework.stereotype.Service;

@Service
public class StorageConditionService extends AbstractBaseService<StorageCondition, StorageConditionDto, Integer> {
    public StorageConditionService(StorageConditionMapper mapper, StorageConditionRepository repository) {
        super(mapper, repository);
    }
}