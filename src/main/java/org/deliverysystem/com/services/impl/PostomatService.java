package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.PostomatDto;
import org.deliverysystem.com.dtos.search.PostomatSearchCriteria;
import org.deliverysystem.com.entities.Postomat;
import org.deliverysystem.com.mappers.PostomatMapper;
import org.deliverysystem.com.repositories.PostomatRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostomatService extends AbstractBaseService<Postomat, PostomatDto, Integer> {
    private final PostomatRepository postomatRepository;

    public PostomatService(PostomatRepository repository, PostomatMapper mapper) {
        super(mapper, repository);
        this.postomatRepository = repository;
    }

    @Transactional(readOnly = true)
    public Page<PostomatDto> findAll(PostomatSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return postomatRepository.findAll(pageable).map(mapper::toDto);
        }

        Specification<Postomat> spec = Specification.where(SpecificationUtils.<Postomat>iLike("deliveryPoint.name", criteria.name()))
                .and(SpecificationUtils.iLike("code", criteria.code()))
                .and(SpecificationUtils.iLike("deliveryPoint.address", criteria.address()))
                .and(SpecificationUtils.equal("deliveryPoint.city.id", criteria.cityId()))
                .and(SpecificationUtils.equal("isActive", criteria.isActive()))
                .and(SpecificationUtils.gte("cellsCount", criteria.cellsCountMin()))
                .and(SpecificationUtils.lte("cellsCount", criteria.cellsCountMax()));

        return postomatRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PostomatDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return postomatRepository.findAllByCityId(cityId, pageable).map(mapper::toDto);
    }
}