package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.dtos.search.BranchSearchCriteria;
import org.deliverysystem.com.entities.Branch;
import org.deliverysystem.com.mappers.BranchMapper;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService extends AbstractBaseService<Branch, BranchDto, Integer> {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository repository, BranchMapper mapper) {
        super(mapper, repository);
        this.branchRepository = repository;
    }

    @Transactional(readOnly = true)
    public Page<BranchDto> findAll(BranchSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return branchRepository.findAll(pageable).map(mapper::toDto);
        }

        Specification<Branch> spec = Specification.where(SpecificationUtils.<Branch>iLike("deliveryPoint.name", criteria.name()))
                .and(SpecificationUtils.iLike("deliveryPoint.address", criteria.address()))
                .and(SpecificationUtils.equal("deliveryPoint.city.id", criteria.cityId()))
                .and(SpecificationUtils.equal("branchType.id", criteria.branchTypeId()));

        return branchRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<BranchDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return branchRepository.findAllByCityId(cityId, pageable).map(mapper::toDto);
    }
}