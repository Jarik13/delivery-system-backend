package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.FleetBrandDto;
import org.deliverysystem.com.entities.FleetBrand;
import org.deliverysystem.com.mappers.FleetBrandMapper;
import org.deliverysystem.com.repositories.FleetBrandRepository;
import org.springframework.stereotype.Service;

@Service
public class FleetBrandService extends AbstractBaseService<FleetBrand, FleetBrandDto, Integer> {
    public FleetBrandService(FleetBrandMapper mapper, FleetBrandRepository repository) {
        super(mapper, repository);
    }
}