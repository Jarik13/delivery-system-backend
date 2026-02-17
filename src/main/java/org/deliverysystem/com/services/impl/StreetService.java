package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.StreetDto;
import org.deliverysystem.com.entities.Street;
import org.deliverysystem.com.mappers.StreetMapper;
import org.deliverysystem.com.repositories.StreetRepository;
import org.deliverysystem.com.utils.RestPage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StreetService extends AbstractBaseService<Street, StreetDto, Integer> {
    private final StreetRepository streetRepository;

    public StreetService(StreetRepository repository, StreetMapper mapper) {
        super(mapper, repository);
        this.streetRepository = repository;
    }

    @Transactional(readOnly = true)
    public RestPage<StreetDto> findAllByCityId(Integer cityId, Pageable pageable) {
        return new RestPage<>(streetRepository.findAllByCityId(cityId, pageable).map(mapper::toDto));
    }
}