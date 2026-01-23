package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.AddressDto;
import org.deliverysystem.com.entities.Address;
import org.deliverysystem.com.mappers.AddressMapper;
import org.deliverysystem.com.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends AbstractBaseService<Address, AddressDto, Integer> {
    public AddressService(AddressRepository repository, AddressMapper mapper) {
        super(mapper, repository);
    }
}