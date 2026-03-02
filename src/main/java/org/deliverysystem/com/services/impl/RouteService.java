package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.routes.CreateRouteDto;
import org.deliverysystem.com.dtos.routes.RouteDto;
import org.deliverysystem.com.dtos.routes.RouteStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteSearchCriteria;
import org.deliverysystem.com.entities.Route;
import org.deliverysystem.com.mappers.RouteMapper;
import org.deliverysystem.com.repositories.RouteRepository;
import org.deliverysystem.com.repositories.BranchRepository;
import org.deliverysystem.com.utils.RestPage;
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
    private final RouteMapper routeMapper;

    public RouteService(RouteRepository repository, RouteMapper mapper, BranchRepository branchRepository) {
        super(mapper, repository);
        this.routeRepository = repository;
        this.branchRepository = branchRepository;
        this.routeMapper = mapper;
    }

    @Transactional
    public RouteDto create(CreateRouteDto dto) {
        Route entity = new Route();
        applyDtoToEntity(entity, dto);
        return routeMapper.toDto(routeRepository.save(entity));
    }

    @Transactional
    public RouteDto update(Integer id, CreateRouteDto dto) {
        Route entity = routeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND_FOR_UPDATE));
        applyDtoToEntity(entity, dto);
        return routeMapper.toDto(routeRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public RestPage<RouteDto> findAll(RouteSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            Page<RouteDto> result = routeRepository.findAll(pageable).map(routeMapper::toDto);
            return new RestPage<>(result);
        }

        Specification<Route> spec = Specification
                .where(SpecificationUtils.<Route>equal("originBranch.id", criteria.originBranchId()))
                .and(SpecificationUtils.equal("destinationBranch.id", criteria.destinationBranchId()))
                .and(SpecificationUtils.iLike("originBranch.deliveryPoint.name", criteria.originBranchName()))
                .and(SpecificationUtils.iLike("destinationBranch.deliveryPoint.name", criteria.destinationBranchName()))
                .and(SpecificationUtils.gte("distanceKm", criteria.distanceKmMin()))
                .and(SpecificationUtils.lte("distanceKm", criteria.distanceKmMax()))
                .and(SpecificationUtils.equal("needSorting", criteria.needSorting()));

        Page<RouteDto> result = routeRepository.findAll(spec, pageable).map(routeMapper::toDto);
        return new RestPage<>(result);
    }

    @Transactional(readOnly = true)
    public RouteStatisticsDto getStatistics() {
        return new RouteStatisticsDto(routeRepository.findMinDistanceKm(), routeRepository.findMaxDistanceKm());
    }

    @Transactional(readOnly = true)
    public RestPage<RouteDto> findAllByOriginBranchId(Integer branchId, Pageable pageable) {
        return new RestPage<>(routeRepository.findAllByOriginBranchId(branchId, pageable).map(routeMapper::toDto));
    }

    private void applyDtoToEntity(Route entity, CreateRouteDto dto) {
        entity.setOriginBranch(
                branchRepository.findById(dto.originBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Відділення відправлення не знайдено"))
        );
        entity.setDestinationBranch(
                branchRepository.findById(dto.destinationBranchId())
                        .orElseThrow(() -> new EntityNotFoundException("Відділення призначення не знайдено"))
        );
        entity.setNeedSorting(dto.needSorting() != null ? dto.needSorting() : false);
        entity.setDistanceKm(dto.distanceKm());
    }
}