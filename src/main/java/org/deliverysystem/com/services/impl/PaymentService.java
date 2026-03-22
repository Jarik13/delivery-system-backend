package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.dtos.payments.CreatePaymentDto;
import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.dtos.payments.PaymentStatisticDto;
import org.deliverysystem.com.dtos.search.PaymentSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.Payment;
import org.deliverysystem.com.entities.Shipment;
import org.deliverysystem.com.mappers.PaymentMapper;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.deliverysystem.com.repositories.PaymentRepository;
import org.deliverysystem.com.repositories.PaymentTypeRepository;
import org.deliverysystem.com.repositories.ShipmentRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService extends AbstractBaseService<Payment, PaymentDto, Integer> {
    private final PaymentRepository paymentRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper mapper,
                          EmployeeRepository employeeRepository,
                          ShipmentRepository shipmentRepository,
                          PaymentTypeRepository paymentTypeRepository) {
        super(mapper, paymentRepository);
        this.paymentRepository = paymentRepository;
        this.employeeRepository = employeeRepository;
        this.shipmentRepository = shipmentRepository;
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Transactional
    @CacheEvict(value = {"paymentStatistics", "shipmentPages"}, allEntries = true)
    public PaymentDto createPayment(CreatePaymentDto dto) {
        Shipment shipment = shipmentRepository.findById(dto.shipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Відправлення не знайдено: " + dto.shipmentId()));

        Payment payment = new Payment();
        payment.setShipment(shipment);
        payment.setAmount(dto.amount());
        payment.setPaymentType(paymentTypeRepository.findById(dto.paymentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Тип оплати не знайдено: " + dto.paymentTypeId())));

        return mapper.toDto(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public RestPage<PaymentDto> findAll(PaymentSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch() != null ? e.getBranch().getId() : null)
                .orElse(null);

        Specification<Payment> spec = Specification.where(byBranch(branchId));

        if (criteria != null) {
            spec = spec
                    .and(SpecificationUtils.iLike("transactionNumber", criteria.transactionNumber()))
                    .and(SpecificationUtils.iLike("shipment.trackingNumber", criteria.shipmentTrackingNumber()))
                    .and(SpecificationUtils.equal("shipment.id", criteria.shipmentId()))
                    .and(SpecificationUtils.in("paymentType.id", criteria.paymentTypes()))
                    .and(SpecificationUtils.gte("amount", criteria.amountMin()))
                    .and(SpecificationUtils.lte("amount", criteria.amountMax()))
                    .and(SpecificationUtils.gte("date", criteria.paymentDateFrom()))
                    .and(SpecificationUtils.lte("date", criteria.paymentDateTo()));
        }

        return new RestPage<>(paymentRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "paymentStatistics", key = "'global'")
    public PaymentStatisticDto getStatistics() {
        BigDecimal amountMin = paymentRepository.getMinAmount();
        BigDecimal amountMax = paymentRepository.getMaxAmount();

        return new PaymentStatisticDto(amountMin, amountMax);
    }

    private Specification<Payment> byBranch(Integer branchId) {
        if (branchId == null) return (root, query, cb) -> cb.conjunction();
        return (root, query, cb) -> cb.or(
                cb.equal(
                        root.join("shipment").join("originDeliveryPoint").join("deliveryPoint").join("branch").get("id"),
                        branchId
                ),
                cb.equal(
                        root.join("shipment").join("destinationDeliveryPoint").join("deliveryPoint").join("branch").get("id"),
                        branchId
                )
        );
    }
}