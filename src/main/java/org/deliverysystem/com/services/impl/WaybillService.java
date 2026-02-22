package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.search.WaybillSearchCriteria;
import org.deliverysystem.com.dtos.waybills.CreateWaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.mappers.WaybillMapper;
import org.deliverysystem.com.repositories.*;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
public class WaybillService extends AbstractBaseService<Waybill, WaybillDto, Integer> {
    private final WaybillRepository waybillRepository;
    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final ShipmentRepository shipmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipmentWaybillRepository shipmentWaybillRepository;
    private final WaybillRouteRepository waybillRouteRepository;

    public WaybillService(
            WaybillRepository repository,
            WaybillMapper mapper,
            TripRepository tripRepository,
            RouteRepository routeRepository,
            ShipmentRepository shipmentRepository,
            EmployeeRepository employeeRepository,
            ShipmentWaybillRepository shipmentWaybillRepository,
            WaybillRouteRepository waybillRouteRepository
    ) {
        super(mapper, repository);
        this.waybillRepository = repository;
        this.tripRepository = tripRepository;
        this.routeRepository = routeRepository;
        this.shipmentRepository = shipmentRepository;
        this.employeeRepository = employeeRepository;
        this.shipmentWaybillRepository = shipmentWaybillRepository;
        this.waybillRouteRepository = waybillRouteRepository;
    }

    @Transactional(readOnly = true)
    public RestPage<WaybillDto> findAll(WaybillSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return new RestPage<>(waybillRepository.findAll(pageable).map(mapper::toDto));
        }

        Specification<Waybill> spec = Specification
                .where(SpecificationUtils.<Waybill>equal("number", criteria.number()))
                .and(SpecificationUtils.gte("totalWeight", criteria.totalWeightMin()))
                .and(SpecificationUtils.lte("totalWeight", criteria.totalWeightMax()))
                .and(SpecificationUtils.gte("createdAt", criteria.createdAtFrom()))
                .and(SpecificationUtils.lte("createdAt", criteria.createdAtTo()))
                .and(SpecificationUtils.equal("createdBy.id", criteria.createdById()));

        return new RestPage<>(waybillRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    public WaybillDto findByNumber(Integer number) {
        return waybillRepository.findByNumber(number)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.WAYBILL_NOT_FOUND_BY_NUMBER + number));
    }

    @Transactional(readOnly = true)
    public List<WaybillDto> findAllForExport(Integer number) {
        if (number != null) {
            return waybillRepository.findByNumber(number).map(w -> List.of(mapper.toDto(w))).orElse(List.of());
        }
        return waybillRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<WaybillDto> findAllByIds(List<Integer> ids) {
        return waybillRepository.findAllById(ids).stream().map(mapper::toDto).toList();
    }

    @Transactional
    public WaybillDto create(CreateWaybillDto dto) {
        Trip trip = tripRepository.findById(dto.tripId())
                .orElseThrow(() -> new EntityNotFoundException("Рейс не знайдено: " + dto.tripId()));

        Route route = routeRepository.findById(dto.routeId())
                .orElseThrow(() -> new EntityNotFoundException("Маршрут не знайдено: " + dto.routeId()));

        boolean segmentExists = waybillRouteRepository.existsByTripIdAndRouteIdAndWaybillIsNotNull(dto.tripId(), dto.routeId());
        if (segmentExists) {
            throw new IllegalStateException("Накладна для цього сегменту рейсу вже існує");
        }

        List<Shipment> shipments = shipmentRepository.findAllById(dto.shipmentIds());
        if (shipments.size() != dto.shipmentIds().size()) {
            throw new EntityNotFoundException("Деякі відправлення не знайдено");
        }

//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Employee createdBy = employeeRepository.findByUsername(username)
//                .orElseThrow(() -> new EntityNotFoundException("Співробітника не знайдено: " + username));

        // 5. Розраховуємо загальну вагу
        BigDecimal totalWeight = shipments.stream()
                .map(s -> s.getPrice().getWeight()
                        .divide(new BigDecimal("4.5"), 2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 6. Генеруємо номер накладної (наступний після максимального)
        Integer nextNumber = waybillRepository.findMaxNumber()
                .map(max -> max + 1)
                .orElse(300001);

        Waybill waybill = new Waybill();
        waybill.setNumber(nextNumber);
        waybill.setTotalWeight(totalWeight);
        waybill.setVolume(BigDecimal.ZERO);
        waybill.setCreatedBy(null);

        Waybill saved = waybillRepository.save(waybill);

        WaybillRoute waybillRoute = new WaybillRoute();
        waybillRoute.setWaybill(saved);
        waybillRoute.setTrip(trip);
        waybillRoute.setRoute(route);
        waybillRoute.setSequenceNumber(dto.tripSequenceNumber());
        waybillRouteRepository.save(waybillRoute);

        int nextSeq = shipmentWaybillRepository.findMaxSequenceNumberByWaybillId(saved.getId())
                .map(max -> max + 1)
                .orElse(1);

        for (int i = 0; i < shipments.size(); i++) {
            ShipmentWaybill sw = new ShipmentWaybill();
            sw.setShipment(shipments.get(i));
            sw.setWaybill(saved);
            sw.setSequenceNumber(nextSeq + i);
            shipmentWaybillRepository.save(sw);
        }

        log.info("Створено накладну #{} для рейсу {} сегмент {} з {} відправленнями", nextNumber, dto.tripId(), dto.routeId(), shipments.size());

        return mapper.toDto(saved);
    }
}