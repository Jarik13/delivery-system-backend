package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.entities.Route;
import org.deliverysystem.com.mappers.RouteMapper;
import org.deliverysystem.com.repositories.RouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService extends AbstractBaseService<Route, RouteDto, Integer> {
    public RouteService(RouteRepository repository, RouteMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Page<RouteDto> findAllByOriginBranchId(Integer branchId, Pageable pageable) {
        return ((RouteRepository) repository).findAllByOriginBranchId(branchId, pageable).map(mapper::toDto);
    }
}