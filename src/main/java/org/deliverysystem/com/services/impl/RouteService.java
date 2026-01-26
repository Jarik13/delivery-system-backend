package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.dtos.search.RouteSearchCriteria;
import org.deliverysystem.com.entities.Route;
import org.deliverysystem.com.mappers.RouteMapper;
import org.deliverysystem.com.repositories.RouteRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService extends AbstractBaseService<Route, RouteDto, Integer> {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository repository, RouteMapper mapper) {
        super(mapper, repository);
        this.routeRepository = repository;
    }

    @Transactional(readOnly = true)
    public Page<RouteDto> findAll(RouteSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return routeRepository.findAll(pageable).map(mapper::toDto);
        }

        Specification<Route> spec = Specification.where(SpecificationUtils.<Route>equal("originBranch.id", criteria.originBranchId()))
                .and(SpecificationUtils.equal("destinationBranch.id", criteria.destinationBranchId()))
                .and(SpecificationUtils.iLike("originBranch.deliveryPoint.name", criteria.originBranchName()))
                .and(SpecificationUtils.iLike("destinationBranch.deliveryPoint.name", criteria.destinationBranchName()))
                .and(SpecificationUtils.equal("needSorting", criteria.needSorting()));

        return routeRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RouteDto> findAllByOriginBranchId(Integer branchId, Pageable pageable) {
        return routeRepository.findAllByOriginBranchId(branchId, pageable).map(mapper::toDto);
    }
}