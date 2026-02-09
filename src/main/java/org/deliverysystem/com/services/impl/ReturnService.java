package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.entities.Return;
import org.deliverysystem.com.mappers.ReturnMapper;
import org.deliverysystem.com.repositories.ReturnRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnService extends AbstractBaseService<Return, ReturnDto, Integer> {
    public ReturnService(ReturnRepository repo, ReturnMapper mapper) {
        super(mapper, repo);
    }

    @Transactional(readOnly = true)
    public ReturnDto findByTrackingNumber(String trackingNumber) {
        return ((ReturnRepository) repository).findByTrackingNumber(trackingNumber).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RETURN_NOT_FOUND_BY_TRACKING + trackingNumber));
    }
}
