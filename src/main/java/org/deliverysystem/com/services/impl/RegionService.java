package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.RegionDto;
import org.deliverysystem.com.entities.Region;
import org.deliverysystem.com.mappers.RegionMapper;
import org.deliverysystem.com.repositories.RegionRepository;
import org.springframework.stereotype.Service;

@Service
public class RegionService extends AbstractBaseService<Region, RegionDto, Integer> {
    public RegionService(RegionRepository repository, RegionMapper mapper) {
        super(mapper, repository);
    }
}