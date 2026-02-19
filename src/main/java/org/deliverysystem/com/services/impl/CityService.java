package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.CityDto;
import org.deliverysystem.com.dtos.search.CitySearchCriteria;
import org.deliverysystem.com.entities.City;
import org.deliverysystem.com.mappers.CityMapper;
import org.deliverysystem.com.repositories.CityRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService extends AbstractBaseService<City, CityDto, Integer> {
    private final CityRepository cityRepository;

    public CityService(CityRepository repository, CityMapper mapper) {
        super(mapper, repository);
        this.cityRepository = repository;
    }

    @Transactional(readOnly = true)
    public Page<CityDto> findAllByDistrictId(Integer districtId, Pageable pageable) {
        return cityRepository.findAllByDistrictId(districtId, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public RestPage<CityDto> findAll(CitySearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            Page<CityDto> result = repository.findAll(pageable).map(mapper::toDto);
            return new RestPage<>(result);
        }

        Specification<City> spec = Specification.where(SpecificationUtils.<City>iLike("name", criteria.name()))
                .and(SpecificationUtils.equal("district.id", criteria.districtId()))
                .and(SpecificationUtils.equal("district.region.id", criteria.regionId()));

        Page<CityDto> result = cityRepository.findAll(spec, pageable).map(mapper::toDto);
        return new RestPage<>(result);
    }
}