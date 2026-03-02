package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.entities.RouteList;
import org.deliverysystem.com.mappers.RouteListMapper;
import org.deliverysystem.com.repositories.RouteListRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteListService extends AbstractBaseService<RouteList, RouteListDto, Integer> {
    private final RouteListRepository routeListRepository;

    public RouteListService(RouteListRepository repository, RouteListMapper mapper) {
        super(mapper, repository);
        this.routeListRepository = repository;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "routeListPages", key = "{#criteria, #pageable}", condition = "#pageable.pageNumber < 10")
    public RestPage<RouteListDto> findAll(RouteListSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return new RestPage<>(routeListRepository.findAll(pageable).map(mapper::toDto));
        }

        Specification<RouteList> spec = Specification
                .where(SpecificationUtils.<RouteList>equal("number", criteria.number()))
                .and(SpecificationUtils.equal("courier.id", criteria.courierId()))
                .and(SpecificationUtils.in("status.id", criteria.statuses()))
                .and(SpecificationUtils.gte("totalWeight", criteria.totalWeightMin()))
                .and(SpecificationUtils.lte("totalWeight", criteria.totalWeightMax()));

        return new RestPage<>(routeListRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "routeListStatistics", key = "'global'")
    public RouteListStatisticsDto getStatistics() {
        return new RouteListStatisticsDto(
                routeListRepository.getMinTotalWeight(),
                routeListRepository.getMaxTotalWeight()
        );
    }
}