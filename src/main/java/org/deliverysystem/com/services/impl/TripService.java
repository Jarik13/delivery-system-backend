package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.TripDto;
import org.deliverysystem.com.entities.Trip;
import org.deliverysystem.com.mappers.TripMapper;
import org.deliverysystem.com.repositories.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TripService extends AbstractBaseService<Trip, TripDto, Integer> {
    public TripService(TripRepository repository, TripMapper mapper) {
        super(mapper, repository);
    }
}