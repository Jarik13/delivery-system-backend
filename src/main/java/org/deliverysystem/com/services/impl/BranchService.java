package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.entities.Branch;
import org.deliverysystem.com.mappers.BranchMapper;
import org.deliverysystem.com.repositories.BranchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService extends AbstractBaseService<Branch, BranchDto, Integer> {
    public BranchService(BranchRepository repository, BranchMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<BranchDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return ((BranchRepository) repository).findAllByCityId(cityId, pageable).map(mapper::toDto);
    }
}