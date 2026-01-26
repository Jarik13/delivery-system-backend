package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.PostomatDto;
import org.deliverysystem.com.dtos.search.PostomatSearchCriteria;
import org.deliverysystem.com.entities.Postomat;
import org.deliverysystem.com.mappers.PostomatMapper;
import org.deliverysystem.com.repositories.PostomatRepository;
import org.deliverysystem.com.repositories.CityRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostomatService extends AbstractBaseService<Postomat, PostomatDto, Integer> {
    private final PostomatRepository postomatRepository;
    private final CityRepository cityRepository;

    public PostomatService(PostomatRepository repository, PostomatMapper mapper, CityRepository cityRepository) {
        super(mapper, repository);
        this.postomatRepository = repository;
        this.cityRepository = cityRepository;
    }

    @Override
    public PostomatDto create(PostomatDto dto) {
        Postomat entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDto(postomatRepository.save(entity));
    }

    @Override
    public PostomatDto update(Integer id, PostomatDto dto) {
        if (!postomatRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND_FOR_UPDATE);
        }
        Postomat entity = mapper.toEntity(dto);
        entity.setId(id);
        setRelationships(entity, dto);
        return mapper.toDto(postomatRepository.save(entity));
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

    private void setRelationships(Postomat entity, PostomatDto dto) {
        entity.getDeliveryPoint().setCity(cityRepository.findById(dto.cityId()).orElseThrow(() -> new EntityNotFoundException("City not found")));
    }
}