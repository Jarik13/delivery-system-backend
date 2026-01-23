package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.DriverDto;
import org.deliverysystem.com.entities.Driver;
import org.deliverysystem.com.mappers.DriverMapper;
import org.deliverysystem.com.repositories.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService extends AbstractBaseService<Driver, DriverDto, Integer> {
    public DriverService(DriverRepository repository, DriverMapper mapper) {
        super(mapper, repository);
    }
}