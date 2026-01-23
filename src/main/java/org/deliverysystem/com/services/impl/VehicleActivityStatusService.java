package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.VehicleActivityStatusDto;
import org.deliverysystem.com.entities.VehicleActivityStatus;
import org.deliverysystem.com.mappers.VehicleActivityStatusMapper;
import org.deliverysystem.com.repositories.VehicleActivityStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleActivityStatusService extends AbstractBaseService<VehicleActivityStatus, VehicleActivityStatusDto, Integer> {
    public VehicleActivityStatusService(VehicleActivityStatusMapper mapper, VehicleActivityStatusRepository repository) {
        super(mapper, repository);
    }
}