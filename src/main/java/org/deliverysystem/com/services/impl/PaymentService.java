package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.dtos.search.PaymentSearchCriteria;
import org.deliverysystem.com.entities.Payment;
import org.deliverysystem.com.mappers.PaymentMapper;
import org.deliverysystem.com.repositories.PaymentRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService extends AbstractBaseService<Payment, PaymentDto, Integer> {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository repo, PaymentMapper mapper) {
        super(mapper, repo);
        this.paymentRepository = repo;
    }

    @Transactional(readOnly = true)
    public RestPage<PaymentDto> findAll(PaymentSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            Page<PaymentDto> result = paymentRepository.findAll(pageable).map(mapper::toDto);
            return new RestPage<>(result);
        }

        Specification<Payment> spec = Specification
                .where(SpecificationUtils.<Payment>iLike("transactionNumber", criteria.transactionNumber()))
                .and(SpecificationUtils.iLike("shipment.trackingNumber", criteria.shipmentTrackingNumber()))
                .and(SpecificationUtils.equal("shipment.id", criteria.shipmentId()))
                .and(SpecificationUtils.in("paymentType.id", criteria.paymentTypes()))
                .and(SpecificationUtils.gte("amount", criteria.amountMin()))
                .and(SpecificationUtils.lte("amount", criteria.amountMax()))
                .and(SpecificationUtils.gte("date", criteria.paymentDateFrom()))
                .and(SpecificationUtils.lte("date", criteria.paymentDateTo()));

        Page<PaymentDto> result = paymentRepository.findAll(spec, pageable).map(mapper::toDto);
        return new RestPage<>(result);
    }
}