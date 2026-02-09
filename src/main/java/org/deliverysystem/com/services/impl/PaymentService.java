package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.entities.Payment;
import org.deliverysystem.com.mappers.PaymentMapper;
import org.deliverysystem.com.repositories.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService extends AbstractBaseService<Payment, PaymentDto, Integer> {
    public PaymentService(PaymentRepository repo, PaymentMapper mapper) {
        super(mapper, repo);
    }

    @Transactional(readOnly = true)
    public PaymentDto findByTransactionNumber(String transactionNumber) {
        return ((PaymentRepository) repository).findByTransactionNumber(transactionNumber).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND_BY_TRANSACTION + transactionNumber));
    }

    @Transactional(readOnly = true)
    public Page<PaymentDto> findAllByShipmentId(Integer shipmentId, Pageable pageable) {
        return ((PaymentRepository) repository).findAllByShipmentId(shipmentId, pageable).map(mapper::toDto);
    }
}
