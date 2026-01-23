package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.ReturnReasonDto;
import org.deliverysystem.com.entities.ReturnReason;
import org.deliverysystem.com.mappers.ReturnReasonMapper;
import org.deliverysystem.com.repositories.ReturnReasonRepository;
import org.springframework.stereotype.Service;

@Service
public class ReturnReasonService extends AbstractBaseService<ReturnReason, ReturnReasonDto, Integer> {
    public ReturnReasonService(ReturnReasonMapper mapper, ReturnReasonRepository repository) {
        super(mapper, repository);
    }
}