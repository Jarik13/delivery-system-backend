package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.CourierDto;
import org.deliverysystem.com.entities.Courier;
import org.deliverysystem.com.mappers.CourierMapper;
import org.deliverysystem.com.repositories.CourierRepository;
import org.springframework.stereotype.Service;

@Service
public class CourierService extends AbstractBaseService<Courier, CourierDto, Integer> {
    public CourierService(CourierRepository repository, CourierMapper mapper) {
        super(mapper, repository);
    }
}