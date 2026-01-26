package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.BranchDto;
import org.deliverysystem.com.dtos.search.BranchSearchCriteria;
import org.deliverysystem.com.entities.Branch;
import org.deliverysystem.com.mappers.BranchMapper;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.repositories.CityRepository;
import org.deliverysystem.com.repositories.BranchTypeRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService extends AbstractBaseService<Branch, BranchDto, Integer> {
    private final BranchRepository branchRepository;
    private final CityRepository cityRepository;
    private final BranchTypeRepository branchTypeRepository;

    public BranchService(BranchRepository repository, BranchMapper mapper,
                         CityRepository cityRepository, BranchTypeRepository branchTypeRepository) {
        super(mapper, repository);
        this.branchRepository = repository;
        this.cityRepository = cityRepository;
        this.branchTypeRepository = branchTypeRepository;
    }

    @Override
    public BranchDto create(BranchDto dto) {
        Branch entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDto(branchRepository.save(entity));
    }

    @Override
    public BranchDto update(Integer id, BranchDto dto) {
        if (!branchRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND_FOR_UPDATE);
        }
        Branch entity = mapper.toEntity(dto);
        entity.setId(id);
        setRelationships(entity, dto);
        return mapper.toDto(branchRepository.save(entity));
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

    private void setRelationships(Branch entity, BranchDto dto) {
        entity.getDeliveryPoint().setCity(cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("City not found")));
        entity.setBranchType(branchTypeRepository.findById(dto.branchTypeId()).orElseThrow(() -> new EntityNotFoundException("Branch Type not found")));
    }
}