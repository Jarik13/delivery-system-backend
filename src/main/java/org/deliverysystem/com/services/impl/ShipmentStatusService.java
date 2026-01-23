package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.ShipmentStatusDto;
import org.deliverysystem.com.entities.ShipmentStatus;
import org.deliverysystem.com.mappers.ShipmentStatusMapper;
import org.deliverysystem.com.repositories.ShipmentStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentStatusService extends AbstractBaseService<ShipmentStatus, ShipmentStatusDto, Integer> {
    public ShipmentStatusService(ShipmentStatusMapper mapper, ShipmentStatusRepository repository) {
        super(mapper, repository);
    }
}