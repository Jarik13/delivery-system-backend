package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.CityDto;
import org.deliverysystem.com.entities.City;
import org.deliverysystem.com.mappers.CityMapper;
import org.deliverysystem.com.repositories.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService extends AbstractBaseService<City, CityDto, Integer> {
    public CityService(CityRepository repository, CityMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<CityDto> findAllByDistrictId(Integer districtId, Pageable pageable) {
        return ((CityRepository) repository).findAllByDistrictId(districtId, pageable).map(mapper::toDto);
    }
}