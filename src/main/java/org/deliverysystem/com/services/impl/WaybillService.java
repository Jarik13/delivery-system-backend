package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.dtos.search.WaybillSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.dtos.waybills.*;
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
    public RestPage<WaybillDto> findAll(WaybillSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Specification<Waybill> spec = Specification.where(SpecificationUtils.equal("createdBy.id", user.id()));

        if (criteria != null) {
            spec = spec
                    .and(SpecificationUtils.equal("number", criteria.number()))
                    .and(SpecificationUtils.gte("totalWeight", criteria.totalWeightMin()))
                    .and(SpecificationUtils.lte("totalWeight", criteria.totalWeightMax()))
                    .and(SpecificationUtils.gte("createdAt", criteria.createdAtFrom()))
                    .and(SpecificationUtils.lte("createdAt", criteria.createdAtTo()))
                    .and(SpecificationUtils.equal("createdBy.id", criteria.createdById()));
        }

        return new RestPage<>(waybillRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    public WaybillStatisticsDto getStatistics() {
        BigDecimal totalWeightMin = waybillRepository.findMinTotalWeight().orElse(BigDecimal.ZERO);
        BigDecimal totalWeightMax = waybillRepository.findMaxTotalWeight().orElse(new BigDecimal("5000"));
        BigDecimal volumeMin = waybillRepository.findMinVolume().orElse(BigDecimal.ZERO);
        BigDecimal volumeMax = waybillRepository.findMaxVolume().orElse(new BigDecimal("5000"));

        return new WaybillStatisticsDto(totalWeightMin, totalWeightMax, volumeMin, volumeMax);
    }

    @Transactional(readOnly = true)
    public WaybillDetailsDto getWaybillDetails(Integer waybillId) {
        Waybill waybill = repository.findById(waybillId)
                .orElseThrow(() -> new EntityNotFoundException("Накладна не знайдена: " + waybillId));

        List<WaybillShipmentDto> shipments = shipmentWaybillRepository
                .findByWaybillIdOrderBySequenceNumber(waybillId)
                .stream()
                .map(sw -> {
                    Shipment s = sw.getShipment();

                    String originCity = null;
                    String destCity = null;

                    if (s.getOriginDeliveryPoint() != null && s.getOriginDeliveryPoint().getDeliveryPoint() != null)
                        originCity = s.getOriginDeliveryPoint().getDeliveryPoint().getCity() != null
                                ? s.getOriginDeliveryPoint().getDeliveryPoint().getCity().getName() : null;

                    if (s.getDestinationDeliveryPoint() != null && s.getDestinationDeliveryPoint().getDeliveryPoint() != null)
                        destCity = s.getDestinationDeliveryPoint().getDeliveryPoint().getCity() != null
                                ? s.getDestinationDeliveryPoint().getDeliveryPoint().getCity().getName() : null;

                    BigDecimal actualWeight = (s.getPrice() != null && s.getPrice().getWeight() != null)
                            ? s.getPrice().getWeight().divide(new BigDecimal("4.5"), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    BigDecimal total = s.getPrice() != null ? s.getPrice().getTotal() : BigDecimal.ZERO;

                    BigDecimal remaining = BigDecimal.ZERO;
                    boolean fullyPaid = false;
                    if (s.getPayments() != null) {
                        BigDecimal paid = s.getPayments().stream()
                                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        fullyPaid = paid.compareTo(total) >= 0;
                        remaining = total.subtract(paid).max(BigDecimal.ZERO);
                    }

                    boolean hasSpecialPackaging = s.getPrice() != null
                                                  && s.getPrice().getSpecialPackaging() != null
                                                  && s.getPrice().getSpecialPackaging().compareTo(BigDecimal.ZERO) > 0;

                    return new WaybillShipmentDto(
                            s.getId(),
                            s.getTrackingNumber(),
                            sw.getSequenceNumber(),
                            formatClientName(s.getSender()),
                            formatClientName(s.getRecipient()),
                            originCity,
                            destCity,
                            actualWeight,
                            total,
                            s.getShipmentStatus() != null ? s.getShipmentStatus().getName() : null,
                            s.getCreatedAt(),
                            s.getShipmentType() != null ? s.getShipmentType().getName() : null,
                            s.getParcel() != null ? s.getParcel().getContentDescription() : null,
                            fullyPaid,
                            remaining,
                            hasSpecialPackaging,
                            s.getIssuedAt()
                    );
                })
                .toList();

        return new WaybillDetailsDto(
                waybill.getId(),
                waybill.getNumber(),
                waybill.getCreatedAt(),
                waybill.getTotalWeight(),
                waybill.getVolume(),
                waybill.getCreatedBy() != null ? formatEmployeeName(waybill.getCreatedBy()) : null,
                shipments
        );
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
    public WaybillDto create(CreateWaybillDto dto, CurrentUserDto user) {
        Trip trip = tripRepository.findById(dto.tripId())
                .orElseThrow(() -> new EntityNotFoundException("Рейс не знайдено: " + dto.tripId()));

        WaybillRoute waybillRoute = waybillRouteRepository.findByTripIdAndSequenceNumber(dto.tripId(), dto.tripSequenceNumber())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Сегмент №" + dto.tripSequenceNumber() + " для рейсу " + dto.tripId() + " не знайдено в плані маршруту"));

        if (waybillRoute.getWaybill() != null) {
            throw new IllegalStateException("Накладна для зупинки №" + dto.tripSequenceNumber() + " вже існує (ID накладної: " + waybillRoute.getWaybill().getId() + ")");
        }

        List<Shipment> shipments = shipmentRepository.findAllById(dto.shipmentIds());
        if (shipments.size() != dto.shipmentIds().size()) {
            throw new EntityNotFoundException("Деякі відправлення зі списку не знайдено в базі даних");
        }

        Employee createdBy = employeeRepository.findById(user.id())
                .orElseThrow(() -> new EntityNotFoundException("Співробітника не знайдено: " + user.id()));

        BigDecimal totalWeight = shipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null
                        ? s.getParcel().getActualWeight()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalVolume = shipments.stream()
                .map(s -> s.getParcel() != null && s.getParcel().getActualWeight() != null
                        ? s.getParcel().getActualWeight().multiply(new BigDecimal("0.005"))
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(4, RoundingMode.HALF_UP);

        Integer nextNumber = waybillRepository.findMaxNumber()
                .map(max -> max + 1)
                .orElse(300001);

        Waybill waybill = new Waybill();
        waybill.setNumber(nextNumber);
        waybill.setTotalWeight(totalWeight);
        waybill.setVolume(totalVolume);
        waybill.setCreatedBy(createdBy);

        Waybill savedWaybill = waybillRepository.save(waybill);

        waybillRoute.setWaybill(savedWaybill);
        waybillRouteRepository.save(waybillRoute);

        int nextSeq = shipmentWaybillRepository.findMaxSequenceNumberByWaybillId(savedWaybill.getId())
                .map(max -> max + 1)
                .orElse(1);

        for (int i = 0; i < shipments.size(); i++) {
            ShipmentWaybill sw = new ShipmentWaybill();
            sw.setShipment(shipments.get(i));
            sw.setWaybill(savedWaybill);
            sw.setSequenceNumber(nextSeq + i);
            shipmentWaybillRepository.save(sw);
        }

        log.info("Успішно оформлено накладну #{} для рейсу {} (зупинка №{})", nextNumber, dto.tripId(), dto.tripSequenceNumber());

        return mapper.toDto(savedWaybill);
    }

    private String formatClientName(Client client) {
        if (client == null) return null;
        return "%s %s %s".formatted(
                nullToEmpty(client.getLastName()),
                nullToEmpty(client.getFirstName()),
                nullToEmpty(client.getMiddleName())
        ).trim();
    }

    private String formatEmployeeName(Employee employee) {
        if (employee == null) return null;
        return "%s %s %s".formatted(
                nullToEmpty(employee.getLastName()),
                nullToEmpty(employee.getFirstName()),
                nullToEmpty(employee.getMiddleName())
        ).trim();
    }

    private String nullToEmpty(String s) {
        return s != null ? s : "";
    }
}