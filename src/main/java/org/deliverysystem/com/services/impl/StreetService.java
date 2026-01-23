package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.StreetDto;
import org.deliverysystem.com.entities.Street;
import org.deliverysystem.com.mappers.StreetMapper;
import org.deliverysystem.com.repositories.StreetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StreetService extends AbstractBaseService<Street, StreetDto, Integer> {
    public StreetService(StreetRepository repository, StreetMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<StreetDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return ((StreetRepository) repository).findAllByCityId(cityId, pageable).map(mapper::toDto);
    }
}