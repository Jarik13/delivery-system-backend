package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.entities.Shipment;
import org.deliverysystem.com.mappers.ShipmentMapper;
import org.deliverysystem.com.repositories.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentService extends AbstractBaseService<Shipment, ShipmentDto, Integer> {
    public ShipmentService(ShipmentRepository repo, ShipmentMapper mapper) {
        super(mapper, repo);
    }

    @Transactional(readOnly = true)
    public ShipmentDto findByTrackingNumber(String trackingNumber) {
        return ((ShipmentRepository) repository).findByTrackingNumber(trackingNumber).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.SHIPMENT_NOT_FOUND_BY_TRACKING + trackingNumber));
    }

    @Transactional(readOnly = true)
    public Page<ShipmentDto> findAllBySenderId(Integer senderId, Pageable pageable) {
        return ((ShipmentRepository) repository).findAllBySenderId(senderId, pageable).map(mapper::toDto);
    }
}
