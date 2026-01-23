package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.PaymentTypeDto;
import org.deliverysystem.com.entities.PaymentType;
import org.deliverysystem.com.mappers.PaymentTypeMapper;
import org.deliverysystem.com.repositories.PaymentTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentTypeService extends AbstractBaseService<PaymentType, PaymentTypeDto, Integer> {
    public PaymentTypeService(PaymentTypeMapper mapper, PaymentTypeRepository repository) {
        super(mapper, repository);
    }
}