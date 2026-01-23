package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.DistrictDto;
import org.deliverysystem.com.entities.District;
import org.deliverysystem.com.mappers.DistrictMapper;
import org.deliverysystem.com.repositories.DistrictRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistrictService extends AbstractBaseService<District, DistrictDto, Integer> {
    public DistrictService(DistrictRepository repository, DistrictMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<DistrictDto> findAllByRegionId(Integer regionId, Pageable pageable) {
        return ((DistrictRepository) repository).findAllByRegionId(regionId, pageable).map(mapper::toDto);
    }
}