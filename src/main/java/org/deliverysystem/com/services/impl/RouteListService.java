package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.route_lists.CreateRouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.exceptions.exceptions.BusinessValidationException;
import org.deliverysystem.com.mappers.RouteListMapper;
import org.deliverysystem.com.repositories.CourierRepository;
import org.deliverysystem.com.repositories.RouteListRepository;
import org.deliverysystem.com.repositories.RouteListStatusRepository;
import org.deliverysystem.com.repositories.ShipmentRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteListService extends AbstractBaseService<RouteList, RouteListDto, Integer> {
    private final RouteListRepository routeListRepository;
    private final ShipmentRepository shipmentRepository;
    private final CourierRepository courierRepository;
    private final RouteListStatusRepository routeListStatusRepository;
    private final RouteListMapper routeListMapper;

    public RouteListService(
            RouteListRepository repository,
            RouteListMapper mapper,
            ShipmentRepository shipmentRepository,
            CourierRepository courierRepository,
            RouteListStatusRepository routeListStatusRepository) {
        super(mapper, repository);
        this.routeListRepository = repository;
        this.routeListMapper = mapper;
        this.shipmentRepository = shipmentRepository;
        this.courierRepository = courierRepository;
        this.routeListStatusRepository = routeListStatusRepository;
    }

    @Transactional
    public RouteListDto createRouteList(CreateRouteListDto dto) {
        List<Shipment> shipments = shipmentRepository.findAllById(dto.shipmentIds());

        if (shipments.size() != dto.shipmentIds().size()) {
            List<Integer> foundIds = shipments.stream().map(Shipment::getId).toList();
            List<Integer> missing = dto.shipmentIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new BusinessValidationException("shipmentIds", "Відправлення не знайдено: " + missing);
        }

        BigDecimal totalWeight = shipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null
                        ? s.getParcel().getActualWeight()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeight.compareTo(new BigDecimal("100.0")) > 0) {
            throw new BusinessValidationException("shipmentIds",
                    "Сумарна вага відправлень перевищує 30 кг (" +
                    totalWeight.setScale(2, RoundingMode.HALF_UP) + " кг)");
        }

        Courier courier = courierRepository.findById(dto.courierId())
                .orElseThrow(() -> new BusinessValidationException("courierId", "Кур'єра з ID " + dto.courierId() + " не знайдено"));

        RouteListStatus status = routeListStatusRepository.findByName("Сформовано").orElseThrow(() -> new IllegalStateException("Статус 'Сформовано' не знайдено в БД"));

        RouteList routeList = new RouteList();
        routeList.setCourier(courier);
        routeList.setStatus(status);
        routeList.setTotalWeight(totalWeight);
        routeList.setPlannedDepartureTime(dto.plannedDepartureTime());

        List<RouteSheetItem> items = shipments.stream().map(shipment -> {
            RouteSheetItem item = new RouteSheetItem();
            item.setRouteList(routeList);
            item.setShipment(shipment);
            item.setDelivered(false);
            return item;
        }).toList();

        routeList.setRouteSheetItems(new ArrayList<>(items));

        RouteList saved = routeListRepository.save(routeList);
        return routeListMapper.toDto(saved);
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
        Map<Integer, Long> countByStatus = routeListRepository.countGroupByStatus()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r[0], r -> (Long) r[1]));

        return new RouteListStatisticsDto(
                routeListRepository.getMinTotalWeight(),
                routeListRepository.getMaxTotalWeight(),
                countByStatus
        );
    }
}