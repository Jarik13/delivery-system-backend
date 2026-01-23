package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.BranchTypeDto;
import org.deliverysystem.com.entities.BranchType;
import org.deliverysystem.com.mappers.BranchTypeMapper;
import org.deliverysystem.com.repositories.BranchTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class BranchTypeService extends AbstractBaseService<BranchType, BranchTypeDto, Integer> {
    public BranchTypeService(BranchTypeMapper mapper, BranchTypeRepository repository) {
        super(mapper, repository);
    }
}