package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.TripStatusDto;
import org.deliverysystem.com.entities.TripStatus;
import org.deliverysystem.com.mappers.TripStatusMapper;
import org.deliverysystem.com.repositories.TripStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class TripStatusService extends AbstractBaseService<TripStatus, TripStatusDto, Integer> {
    public TripStatusService(TripStatusMapper mapper, TripStatusRepository repository) {
        super(mapper, repository);
    }
}