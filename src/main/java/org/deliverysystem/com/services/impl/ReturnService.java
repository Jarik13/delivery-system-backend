package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.dtos.search.ReturnSearchCriteria;
import org.deliverysystem.com.entities.Return;
import org.deliverysystem.com.mappers.ReturnMapper;
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

    public ReturnService(ReturnRepository repository, ReturnMapper mapper) {
        super(mapper, repository);
        this.returnRepository = repository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "returnPages", key = "{#criteria, #pageable}", condition = "#pageable.pageNumber < 10")
    public RestPage<ReturnDto> findAll(ReturnSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return new RestPage<>(returnRepository.findAll(pageable).map(mapper::toDto));
        }

        Specification<Return> spec = Specification
                .where(SpecificationUtils.<Return>iLike("trackingNumber", criteria.returnTrackingNumber()))
                .and(SpecificationUtils.iLike("shipment.trackingNumber", criteria.shipmentTrackingNumber()))
                .and(SpecificationUtils.in("returnReason.id", criteria.returnReasons()))
                .and(SpecificationUtils.gte("initiationDate", criteria.initiationDateFrom()))
                .and(SpecificationUtils.lte("initiationDate", criteria.initiationDateTo()));

        return new RestPage<>(returnRepository.findAll(spec, pageable).map(mapper::toDto));
    }
}
