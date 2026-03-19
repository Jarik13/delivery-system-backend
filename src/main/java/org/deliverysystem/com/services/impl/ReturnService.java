package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.dtos.returns.ReturnStatisticsDto;
import org.deliverysystem.com.dtos.search.ReturnSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.Return;
import org.deliverysystem.com.mappers.ReturnMapper;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.deliverysystem.com.repositories.ReturnRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnService extends AbstractBaseService<Return, ReturnDto, Integer> {
    private final ReturnRepository returnRepository;
    private final EmployeeRepository employeeRepository;

    public ReturnService(ReturnRepository repository, ReturnMapper mapper, EmployeeRepository employeeRepository) {
        super(mapper, repository);
        this.returnRepository = repository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "returnPages", key = "{#criteria, #pageable, #user}", condition = "#pageable.pageNumber < 10")
    public RestPage<ReturnDto> findAll(ReturnSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch() != null ? e.getBranch().getId() : null)
                .orElse(null);

        Specification<Return> spec = Specification.where(byBranch(branchId));

        if (criteria != null) {
            spec = spec
                    .and(SpecificationUtils.iLike("trackingNumber", criteria.returnTrackingNumber()))
                    .and(SpecificationUtils.iLike("shipment.trackingNumber", criteria.shipmentTrackingNumber()))
                    .and(SpecificationUtils.in("returnReason.id", criteria.returnReasons()))
                    .and(SpecificationUtils.gte("initiationDate", criteria.initiationDateFrom()))
                    .and(SpecificationUtils.lte("initiationDate", criteria.initiationDateTo()))
                    .and(SpecificationUtils.gte("refundAmount", criteria.refundAmountMin()))
                    .and(SpecificationUtils.lte("refundAmount", criteria.refundAmountMax()));
        }

        return new RestPage<>(returnRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "returnStatistics", key = "'global'")
    public ReturnStatisticsDto getStatistics() {
        return new ReturnStatisticsDto(
                returnRepository.getMinRefundAmount(),
                returnRepository.getMaxRefundAmount()
        );
    }

    private Specification<Return> byBranch(Integer branchId) {
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
