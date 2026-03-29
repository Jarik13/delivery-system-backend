package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.dtos.route_lists.AddShipmentsToRouteListDto;
import org.deliverysystem.com.dtos.route_lists.CreateRouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteListStatisticsDto;
import org.deliverysystem.com.dtos.search.RouteListSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.exceptions.exceptions.BusinessValidationException;
import org.deliverysystem.com.mappers.RouteListMapper;
import org.deliverysystem.com.repositories.*;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RouteListService extends AbstractBaseService<RouteList, RouteListDto, Integer> {
    private final RouteListRepository routeListRepository;
    private final ShipmentRepository shipmentRepository;
    private final CourierRepository courierRepository;
    private final RouteSheetItemRepository routeSheetItemRepository;
    private final RouteListStatusRepository routeListStatusRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final RouteListMapper routeListMapper;

    public RouteListService(
            RouteListRepository repository,
            RouteListMapper mapper,
            ShipmentRepository shipmentRepository,
            CourierRepository courierRepository,
            RouteSheetItemRepository routeSheetItemRepository,
            ShipmentStatusRepository shipmentStatusRepository,
            RouteListStatusRepository routeListStatusRepository) {
        super(mapper, repository);
        this.routeListRepository = repository;
        this.routeListMapper = mapper;
        this.shipmentRepository = shipmentRepository;
        this.courierRepository = courierRepository;
        this.routeSheetItemRepository = routeSheetItemRepository;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.routeListStatusRepository = routeListStatusRepository;
    }

    @Transactional
    public RouteListDto createRouteList(CreateRouteListDto dto) {
        List<Shipment> shipments = resolveAndValidateShipments(dto.shipmentIds());
        BigDecimal totalWeight = calculateTotalWeight(shipments);

        Courier courier = courierRepository.findById(dto.courierId())
                .orElseThrow(() -> new BusinessValidationException("courierId",
                        "Кур'єра з ID " + dto.courierId() + " не знайдено"));

        RouteListStatus status = routeListStatusRepository.findByName("Сформовано")
                .orElseThrow(() -> new IllegalStateException("Статус 'Сформовано' не знайдено в БД"));

        RouteList routeList = new RouteList();
        routeList.setCourier(courier);
        routeList.setStatus(status);
        routeList.setTotalWeight(totalWeight);
        routeList.setPlannedDepartureTime(dto.plannedDepartureTime());
        routeList.setRouteSheetItems(buildRouteSheetItems(shipments, routeList));

        return routeListMapper.toDto(routeListRepository.save(routeList));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "routeListPages", key = "{#criteria, #pageable, #user}", condition = "#pageable.pageNumber < 10")
    public RestPage<RouteListDto> findAll(RouteListSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Specification<RouteList> accessSpec = "COURIER".equals(user.role())
                ? SpecificationUtils.equal("courier.id", user.id())
                : SpecificationUtils.equal("createdBy.id", user.id());

        Specification<RouteList> spec = Specification
                .where(accessSpec)
                .and(SpecificationUtils.equal("number", criteria != null ? criteria.number() : null))
                .and(SpecificationUtils.equal("courier.id", criteria != null ? criteria.courierId() : null))
                .and(SpecificationUtils.in("status.id", criteria != null ? criteria.statuses() : null))
                .and(SpecificationUtils.gte("totalWeight", criteria != null ? criteria.totalWeightMin() : null))
                .and(SpecificationUtils.lte("totalWeight", criteria != null ? criteria.totalWeightMax() : null));

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

    @Transactional(readOnly = true)
    public List<RouteListDto> findAllByIds(List<Integer> ids) {
        return routeListRepository.findAllById(ids).stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<RouteListDto> findAllForExport(Integer number, CurrentUserDto user) {
        Specification<RouteList> accessSpec = "COURIER".equals(user.role())
                ? SpecificationUtils.equal("courier.id", user.id())
                : SpecificationUtils.equal("createdBy.id", user.id());

        Specification<RouteList> spec = Specification
                .where(accessSpec)
                .and(SpecificationUtils.equal("number", number));

        return routeListRepository.findAll(spec).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "routeListPages", allEntries = true)
    public void updateShipmentDeliveryStatus(Integer itemId, String action) {
        RouteSheetItem item = routeSheetItemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("RouteSheetItem not found"));

        Shipment shipment = item.getShipment();
        LocalDateTime now = LocalDateTime.now();

        switch (action) {
            case "DELIVERED" -> {
                ShipmentStatus status = shipmentStatusRepository.findByName("Доставлено").orElseThrow();
                shipment.setShipmentStatus(status);
                shipment.setIssuedAt(now);
                item.setDelivered(true);
                item.setDeliveredAt(now);
            }
            case "FAILED" -> {
                ShipmentStatus status = shipmentStatusRepository.findByName("Спроба вручення провалена").orElseThrow();
                shipment.setShipmentStatus(status);
                item.setDelivered(false);
                item.setDeliveredAt(null);
            }
            case "REFUSED" -> {
                ShipmentStatus status = shipmentStatusRepository.findByName("Відмова").orElseThrow();
                shipment.setShipmentStatus(status);
                item.setDelivered(false);
                item.setDeliveredAt(null);
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }

        shipmentRepository.save(shipment);
        routeSheetItemRepository.save(item);
    }

    @Transactional
    @CacheEvict(value = {"routeListPages", "routeListStatistics"}, allEntries = true)
    public RouteListDto addShipments(Integer id, AddShipmentsToRouteListDto dto) {
        RouteList routeList = routeListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Маршрутний лист не знайдено: " + id));

        List<Integer> existingShipmentIds = routeList.getRouteSheetItems().stream()
                .map(item -> item.getShipment().getId())
                .toList();

        List<Integer> newIds = dto.shipmentIds().stream()
                .filter(sid -> !existingShipmentIds.contains(sid))
                .toList();

        if (newIds.isEmpty()) {
            return routeListMapper.toDto(routeList);
        }

        List<Shipment> newShipments = resolveAndValidateShipments(newIds);

        BigDecimal existingWeight = routeList.getTotalWeight() != null ? routeList.getTotalWeight() : BigDecimal.ZERO;
        BigDecimal newWeight = newShipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null ? s.getParcel().getActualWeight() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalWeight = existingWeight.add(newWeight);

        if (totalWeight.compareTo(new BigDecimal("100.0")) > 0) {
            throw new BusinessValidationException("shipmentIds", "Сумарна вага перевищує 100 кг (" + totalWeight.setScale(2, RoundingMode.HALF_UP) + " кг)");
        }

        int totalCount = routeList.getRouteSheetItems().size() + newShipments.size();
        if (totalCount > 13) {
            throw new BusinessValidationException("shipmentIds", "Кількість відправлень перевищує 13 (" + totalCount + ")");
        }

        List<RouteSheetItem> newItems = newShipments.stream().map(shipment -> {
            RouteSheetItem item = new RouteSheetItem();
            item.setRouteList(routeList);
            item.setShipment(shipment);
            item.setDelivered(false);
            return item;
        }).toList();

        routeList.getRouteSheetItems().addAll(newItems);
        routeList.setTotalWeight(totalWeight);

        return routeListMapper.toDto(routeListRepository.save(routeList));
    }

    private List<Shipment> resolveAndValidateShipments(List<Integer> shipmentIds) {
        List<Shipment> shipments = shipmentRepository.findAllById(shipmentIds);

        if (shipments.size() != shipmentIds.size()) {
            List<Integer> foundIds = shipments.stream().map(Shipment::getId).toList();
            List<Integer> missing = shipmentIds.stream()
                    .filter(id -> !foundIds.contains(id)).toList();
            throw new BusinessValidationException("shipmentIds", "Відправлення не знайдено: " + missing);
        }
        return shipments;
    }

    private BigDecimal calculateTotalWeight(List<Shipment> shipments) {
        BigDecimal totalWeight = shipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null
                        ? s.getParcel().getActualWeight()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeight.compareTo(new BigDecimal("100.0")) > 0) {
            throw new BusinessValidationException("shipmentIds",
                    "Сумарна вага перевищує 100 кг (" +
                    totalWeight.setScale(2, RoundingMode.HALF_UP) + " кг)");
        }
        return totalWeight;
    }

    private ArrayList<RouteSheetItem> buildRouteSheetItems(List<Shipment> shipments, RouteList routeList) {
        return shipments.stream().map(shipment -> {
            RouteSheetItem item = new RouteSheetItem();
            item.setRouteList(routeList);
            item.setShipment(shipment);
            item.setDelivered(false);
            return item;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}