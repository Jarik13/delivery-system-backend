package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.AddressHouseDto;
import org.deliverysystem.com.entities.AddressHouse;
import org.deliverysystem.com.mappers.AddressHouseMapper;
import org.deliverysystem.com.repositories.AddressHouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressHouseService extends AbstractBaseService<AddressHouse, AddressHouseDto, Integer> {
    public AddressHouseService(AddressHouseRepository repository, AddressHouseMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<AddressHouseDto> findAllByStreetId(Integer streetId, Pageable pageable) {
        return ((AddressHouseRepository) repository).findAllByStreetId(streetId, pageable).map(mapper::toDto);
    }
}