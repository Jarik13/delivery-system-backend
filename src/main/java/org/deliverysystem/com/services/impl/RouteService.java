package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.dtos.routes.RouteStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteSearchCriteria;
import org.deliverysystem.com.entities.Route;
import org.deliverysystem.com.mappers.RouteMapper;
import org.deliverysystem.com.repositories.RouteRepository;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService extends AbstractBaseService<Route, RouteDto, Integer> {
    private final RouteRepository routeRepository;
    private final BranchRepository branchRepository;

    public RouteService(RouteRepository repository, RouteMapper mapper, BranchRepository branchRepository) {
        super(mapper, repository);
        this.routeRepository = repository;
        this.branchRepository = branchRepository;
    }

    @Override
    public RouteDto create(RouteDto dto) {
        Route entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDto(routeRepository.save(entity));
    }

    @Override
    public RouteDto update(Integer id, RouteDto dto) {
        if (!routeRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND_FOR_UPDATE);
        }
        Route entity = mapper.toEntity(dto);
        entity.setId(id);
        setRelationships(entity, dto);
        return mapper.toDto(routeRepository.save(entity));
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
                .and(SpecificationUtils.gte("distanceKm", criteria.distanceKmMin()))
                .and(SpecificationUtils.lte("distanceKm", criteria.distanceKmMax()))
                .and(SpecificationUtils.equal("needSorting", criteria.needSorting()));

        return routeRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    public RouteStatisticsDto getStatistics() {
        Float distanceKmMin = routeRepository.findMinDistanceKm();
        Float distanceKmMax = routeRepository.findMaxDistanceKm();

        return new RouteStatisticsDto(distanceKmMin, distanceKmMax);
    }

    @Transactional(readOnly = true)
    public Page<RouteDto> findAllByOriginBranchId(Integer branchId, Pageable pageable) {
        return routeRepository.findAllByOriginBranchId(branchId, pageable).map(mapper::toDto);
    }

    private void setRelationships(Route entity, RouteDto dto) {
        entity.setOriginBranch(branchRepository.findById(dto.originBranchId()).orElseThrow(() -> new EntityNotFoundException("Origin Branch not found")));
        entity.setDestinationBranch(branchRepository.findById(dto.destinationBranchId()).orElseThrow(() -> new EntityNotFoundException("Destination Branch not found")));
    }
}