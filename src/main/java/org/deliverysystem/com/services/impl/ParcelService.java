package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.ParcelDto;
import org.deliverysystem.com.entities.Parcel;
import org.deliverysystem.com.mappers.ParcelMapper;
import org.deliverysystem.com.repositories.ParcelRepository;
import org.springframework.stereotype.Service;

@Service
public class ParcelService extends AbstractBaseService<Parcel, ParcelDto, Integer> {
    public ParcelService(ParcelRepository repository, ParcelMapper mapper) {
        super(mapper, repository);
    }
}
