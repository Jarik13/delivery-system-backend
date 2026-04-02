package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.route_lists.RouteListShipmentDto;
import org.deliverysystem.com.dtos.search.AvailableShipmentsCriteriaDto;
import org.deliverysystem.com.dtos.search.ShipmentSearchCriteria;
import org.deliverysystem.com.dtos.shipments.*;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.mappers.ShipmentMapper;
import org.deliverysystem.com.repositories.*;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShipmentService extends AbstractBaseService<Shipment, ShipmentDto, Integer> {
    private final ShipmentMapper shipmentMapper;
    private final ShipmentRepository shipmentRepository;
    private final ParcelRepository parcelRepository;
    private final ClientRepository clientRepository;
    private final ShipmentStatusRepository statusRepository;
    private final ShipmentTypeRepository typeRepository;
    private final StorageConditionRepository storageConditionRepository;
    private final BoxVariantRepository boxVariantRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final PaymentRepository paymentRepository;
    private final ParcelTypeRepository parcelTypeRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    private final ShipmentBoxRepository shipmentBoxRepository;
    private final ShipmentOriginDeliveryPointRepository originDeliveryPointRepository;
    private final ShipmentDestinationDeliveryPointRepository destinationDeliveryPointRepository;
    private final ShipmentOriginAddressRepository originAddressRepository;
    private final ShipmentDestinationAddressRepository destinationAddressRepository;
    private final ShipmentWaybillRepository shipmentWaybillRepository;

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final StreetRepository streetRepository;
    private final AddressHouseRepository addressHouseRepository;
    private final AddressRepository addressRepository;
    private final WaybillRouteRepository waybillRouteRepository;
    private final WaybillRouteStatusRepository waybillRouteStatusRepository;
    private final EmployeeRepository employeeRepository;

    public ShipmentService(ShipmentRepository repo, ShipmentMapper mapper,
                           ParcelRepository parcelRepository, ClientRepository clientRepository,
                           ShipmentStatusRepository statusRepository, ShipmentTypeRepository typeRepository,
                           StorageConditionRepository storageConditionRepository, BoxVariantRepository boxVariantRepository, DeliveryPointRepository deliveryPointRepository,
                           PaymentRepository paymentRepository, ParcelTypeRepository parcelTypeRepository, PaymentTypeRepository paymentTypeRepository, ShipmentBoxRepository shipmentBoxRepository,
                           ShipmentOriginDeliveryPointRepository originDeliveryPointRepository, ShipmentDestinationDeliveryPointRepository destinationDeliveryPointRepository, ShipmentOriginAddressRepository originAddressRepository,
                           ShipmentDestinationAddressRepository destinationAddressRepository, TripRepository tripRepository, RouteRepository routeRepository, StreetRepository streetRepository, AddressHouseRepository addressHouseRepository, AddressRepository addressRepository,
                           ShipmentWaybillRepository shipmentWaybillRepository, WaybillRouteRepository waybillRouteRepository, WaybillRouteStatusRepository waybillRouteStatusRepository, EmployeeRepository employeeRepository) {
        super(mapper, repo);
        this.shipmentRepository = repo;
        this.shipmentMapper = mapper;
        this.parcelRepository = parcelRepository;
        this.clientRepository = clientRepository;
        this.statusRepository = statusRepository;
        this.typeRepository = typeRepository;
        this.storageConditionRepository = storageConditionRepository;
        this.boxVariantRepository = boxVariantRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.paymentRepository = paymentRepository;
        this.parcelTypeRepository = parcelTypeRepository;
        this.paymentTypeRepository = paymentTypeRepository;

        this.shipmentBoxRepository = shipmentBoxRepository;
        this.originDeliveryPointRepository = originDeliveryPointRepository;
        this.destinationDeliveryPointRepository = destinationDeliveryPointRepository;
        this.originAddressRepository = originAddressRepository;
        this.destinationAddressRepository = destinationAddressRepository;

        this.tripRepository = tripRepository;
        this.routeRepository = routeRepository;
        this.streetRepository = streetRepository;
        this.addressHouseRepository = addressHouseRepository;
        this.addressRepository = addressRepository;

        this.shipmentWaybillRepository = shipmentWaybillRepository;
        this.waybillRouteRepository = waybillRouteRepository;
        this.waybillRouteStatusRepository = waybillRouteStatusRepository;
        this.employeeRepository = employeeRepository;
    }

    public CalculatedPriceResponseDto calculatePrices(ShipmentPriceCalculationRequestDto req) {
        ShipmentType type = typeRepository.findById(req.shipmentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Type not found"));

        BigDecimal deliveryPrice = type.getName().toLowerCase().contains("експрес")
                ? new BigDecimal("85.00") : new BigDecimal("60.00");

        BigDecimal weightPrice = req.actualWeight().multiply(new BigDecimal("3.5"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal distancePrice = new BigDecimal("300").multiply(new BigDecimal("0.8"))
                .min(new BigDecimal("500.00"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal boxPrice = BigDecimal.ZERO;
        if (req.boxVariantId() != null) {
            boxPrice = boxVariantRepository.findById(req.boxVariantId())
                    .map(BoxVariant::getPrice).orElse(BigDecimal.ZERO);
        }

        BigDecimal specialPackaging = (req.storageConditionIds() != null && !req.storageConditionIds().isEmpty())
                ? new BigDecimal("45.00") : BigDecimal.ZERO;

        BigDecimal insuranceFee = req.declaredValue().multiply(new BigDecimal("0.005"))
                .max(new BigDecimal("5.00"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = deliveryPrice.add(weightPrice).add(distancePrice)
                .add(boxPrice).add(specialPackaging).add(insuranceFee);

        return new CalculatedPriceResponseDto(
                deliveryPrice, weightPrice, distancePrice, boxPrice, specialPackaging, insuranceFee, total
        );
    }

    @Transactional
    @CacheEvict(value = {"shipmentPages", "shipmentStatistics"}, allEntries = true)
    public ShipmentDto createComplexShipment(CreateShipmentDto dto, Integer userId) {
        Parcel parcel = new Parcel();
        parcel.setActualWeight(dto.actualWeight());
        parcel.setDeclaredValue(dto.declaredValue());
        parcel.setContentDescription(dto.contentDescription());
        parcel.setParcelType(parcelTypeRepository.getReferenceById(dto.parcelTypeId()));

        if (dto.storageConditionIds() != null && !dto.storageConditionIds().isEmpty()) {
            parcel.setStorageConditions(new HashSet<>(storageConditionRepository.findAllById(dto.storageConditionIds())));
        }
        parcel = parcelRepository.save(parcel);

        Shipment shipment = new Shipment();
        shipment.setParcel(parcel);
        shipment.setSender(clientRepository.getReferenceById(dto.senderId()));
        shipment.setRecipient(clientRepository.getReferenceById(dto.recipientId()));
        shipment.setShipmentType(typeRepository.getReferenceById(dto.shipmentTypeId()));
        shipment.setShipmentStatus(statusRepository.getReferenceById(1));
        shipment.setSenderPay(dto.isSenderPay());
        shipment.setPartiallyPaid(Boolean.TRUE.equals(dto.isPartiallyPaid()));
        shipment.setCreatedBy(employeeRepository.getReferenceById(userId));

        CalculatedPriceResponseDto p = calculatePrices(new ShipmentPriceCalculationRequestDto(
                dto.contentDescription(),
                dto.actualWeight(),
                dto.declaredValue(),
                dto.parcelTypeId(),
                dto.storageConditionIds(),
                dto.boxVariantId(),
                dto.shipmentTypeId(),
                dto.origin() != null ? dto.origin().cityId() : null,
                dto.destination() != null ? dto.destination().cityId() : null
        ));

        Price price = new Price();
        price.setDelivery(p.deliveryPrice());
        price.setWeight(p.weightPrice());
        price.setDistance(p.distancePrice());
        price.setBoxVariant(p.boxVariantPrice());
        price.setSpecialPackaging(p.specialPackagingPrice());
        price.setInsuranceFee(p.insuranceFee());
        price.setTotal(p.totalPrice());
        shipment.setPrice(price);

        Shipment saved = shipmentRepository.save(shipment);

        saveBridgeLocations(saved, dto.origin(), dto.destination());

        if (dto.boxVariantId() != null) {
            ShipmentBox sb = new ShipmentBox();
            sb.setShipment(saved);
            sb.setBoxVariant(boxVariantRepository.getReferenceById(dto.boxVariantId()));
            shipmentBoxRepository.save(sb);
        }

        if (Boolean.TRUE.equals(dto.isFullyPaid()) && Boolean.TRUE.equals(dto.isSenderPay())) {
            Payment payment = new Payment();
            payment.setShipment(saved);
            payment.setAmount(price.getTotal());
            payment.setDate(LocalDateTime.now());
            if (dto.paymentTypeId() != null) {
                payment.setPaymentType(paymentTypeRepository.getReferenceById(dto.paymentTypeId()));
            }
            paymentRepository.save(payment);
        } else if (Boolean.TRUE.equals(dto.isPartiallyPaid()) && dto.partialAmount() != null) {
            Payment payment = new Payment();
            payment.setShipment(saved);
            payment.setAmount(dto.partialAmount());
            payment.setDate(LocalDateTime.now());
            if (dto.paymentTypeId() != null) {
                payment.setPaymentType(paymentTypeRepository.getReferenceById(dto.paymentTypeId()));
            }
            paymentRepository.save(payment);
        }

        autoAssignToWaybill(saved);

        return mapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(value = {"shipmentPages", "shipmentStatistics", "shipmentMovements"}, allEntries = true)
    public ShipmentDto updateComplexShipment(Integer id, CreateShipmentDto dto, Integer userId) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Відправлення не знайдено: " + id));

        Parcel parcel = shipment.getParcel();
        parcel.setActualWeight(dto.actualWeight());
        parcel.setDeclaredValue(dto.declaredValue());
        parcel.setContentDescription(dto.contentDescription());
        parcel.setParcelType(parcelTypeRepository.getReferenceById(dto.parcelTypeId()));

        if (dto.storageConditionIds() != null && !dto.storageConditionIds().isEmpty()) {
            parcel.setStorageConditions(new HashSet<>(storageConditionRepository.findAllById(dto.storageConditionIds())));
        } else {
            parcel.setStorageConditions(new HashSet<>());
        }
        parcelRepository.save(parcel);

        shipment.setSender(clientRepository.getReferenceById(dto.senderId()));
        shipment.setRecipient(clientRepository.getReferenceById(dto.recipientId()));
        shipment.setShipmentType(typeRepository.getReferenceById(dto.shipmentTypeId()));
        shipment.setSenderPay(dto.isSenderPay());
        shipment.setPartiallyPaid(Boolean.TRUE.equals(dto.isPartiallyPaid()));

        CalculatedPriceResponseDto p = calculatePrices(new ShipmentPriceCalculationRequestDto(
                dto.contentDescription(),
                dto.actualWeight(),
                dto.declaredValue(),
                dto.parcelTypeId(),
                dto.storageConditionIds(),
                dto.boxVariantId(),
                dto.shipmentTypeId(),
                dto.origin() != null ? dto.origin().cityId() : null,
                dto.destination() != null ? dto.destination().cityId() : null
        ));

        Price price = shipment.getPrice() != null ? shipment.getPrice() : new Price();
        price.setDelivery(p.deliveryPrice());
        price.setWeight(p.weightPrice());
        price.setDistance(p.distancePrice());
        price.setBoxVariant(p.boxVariantPrice());
        price.setSpecialPackaging(p.specialPackagingPrice());
        price.setInsuranceFee(p.insuranceFee());
        price.setTotal(p.totalPrice());
        shipment.setPrice(price);

        shipmentRepository.save(shipment);

        deleteExistingLocations(id);
        saveBridgeLocations(shipment, dto.origin(), dto.destination());

        shipmentBoxRepository.deleteByShipmentId(id);
        if (dto.boxVariantId() != null) {
            ShipmentBox sb = new ShipmentBox();
            sb.setShipment(shipment);
            sb.setBoxVariant(boxVariantRepository.getReferenceById(dto.boxVariantId()));
            shipmentBoxRepository.save(sb);
        }

        paymentRepository.deleteByShipmentId(id);
        if (Boolean.TRUE.equals(dto.isFullyPaid()) && Boolean.TRUE.equals(dto.isSenderPay())) {
            Payment payment = new Payment();
            payment.setShipment(shipment);
            payment.setAmount(price.getTotal());
            if (dto.paymentTypeId() != null) {
                payment.setPaymentType(paymentTypeRepository.getReferenceById(dto.paymentTypeId()));
            }
            paymentRepository.save(payment);
        } else if (Boolean.TRUE.equals(dto.isPartiallyPaid()) && dto.partialAmount() != null) {
            Payment payment = new Payment();
            payment.setShipment(shipment);
            payment.setAmount(dto.partialAmount());
            if (dto.paymentTypeId() != null) {
                payment.setPaymentType(paymentTypeRepository.getReferenceById(dto.paymentTypeId()));
            }
            paymentRepository.save(payment);
        }

        return mapper.toDto(shipmentRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shipmentPages", key = "{#criteria, #pageable, #userId}", condition = "#pageable.pageNumber < 5")
    public RestPage<ShipmentDto> findAll(ShipmentSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch().getId())
                .orElse(null);

        Specification<Shipment> spec = Specification.where(byBranch(branchId, user.id()));

        if (criteria != null) {
            spec = spec
                    .and(SpecificationUtils.<Shipment>iLike("trackingNumber", criteria.trackingNumber()))
                    .and(SpecificationUtils.in("shipmentStatus.id", criteria.shipmentStatuses()))
                    .and(SpecificationUtils.in("shipmentType.id", criteria.shipmentTypes()))
                    .and(SpecificationUtils.iLike("parcel.contentDescription", criteria.parcelDescription()))
                    .and(SpecificationUtils.gte("createdAt", criteria.createdAtFrom()))
                    .and(SpecificationUtils.lte("createdAt", criteria.createdAtTo()))
                    .and(SpecificationUtils.gte("issuedAt", criteria.issuedAtFrom()))
                    .and(SpecificationUtils.lte("issuedAt", criteria.issuedAtTo()))
                    .and(SpecificationUtils.gte("parcel.actualWeight", criteria.weightMin()))
                    .and(SpecificationUtils.lte("parcel.actualWeight", criteria.weightMax()))
                    .and(SpecificationUtils.gte("price.total", criteria.totalPriceMin()))
                    .and(SpecificationUtils.lte("price.total", criteria.totalPriceMax()))
                    .and(SpecificationUtils.gte("price.delivery", criteria.deliveryPriceMin()))
                    .and(SpecificationUtils.lte("price.delivery", criteria.deliveryPriceMax()))
                    .and(SpecificationUtils.gte("price.weight", criteria.weightPriceMin()))
                    .and(SpecificationUtils.lte("price.weight", criteria.weightPriceMax()))
                    .and(SpecificationUtils.gte("price.boxVariant", criteria.boxVariantPriceMin()))
                    .and(SpecificationUtils.lte("price.boxVariant", criteria.boxVariantPriceMax()))
                    .and(SpecificationUtils.gte("price.specialPackaging", criteria.specialPackagingPriceMin()))
                    .and(SpecificationUtils.lte("price.specialPackaging", criteria.specialPackagingPriceMax()))
                    .and(SpecificationUtils.gte("price.insuranceFee", criteria.insuranceFeeMin()))
                    .and(SpecificationUtils.lte("price.insuranceFee", criteria.insuranceFeeMax()));
        }

        Page<ShipmentDto> result = shipmentRepository.findAll(spec, pageable).map(mapper::toDto);
        return new RestPage<>(result);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shipmentStatistics", key = "'global'")
    public ShipmentStatisticsDto getStatistics() {
        Map<Integer, Long> countByStatus = shipmentRepository.countGroupByStatus()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r[0], r -> (Long) r[1]));

        Map<Integer, Long> countByType = shipmentRepository.countGroupByType()
                .stream()
                .collect(Collectors.toMap(r -> (Integer) r[0], r -> (Long) r[1]));

        return new ShipmentStatisticsDto(
                defaultIfNull(shipmentRepository.getMinWeight(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxWeight(), new BigDecimal("100")),

                defaultIfNull(shipmentRepository.getMinTotalPrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxTotalPrice(), new BigDecimal("5000")),

                defaultIfNull(shipmentRepository.getMinDeliveryPrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxDeliveryPrice(), new BigDecimal("1000")),

                defaultIfNull(shipmentRepository.getMinWeightPrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxWeightPrice(), new BigDecimal("1000")),

                defaultIfNull(shipmentRepository.getMinDistancePrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxDistancePrice(), new BigDecimal("1000")),

                defaultIfNull(shipmentRepository.getMinBoxVariantPrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxBoxVariantPrice(), new BigDecimal("500")),

                defaultIfNull(shipmentRepository.getMinSpecialPackagingPrice(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxSpecialPackagingPrice(), new BigDecimal("500")),

                defaultIfNull(shipmentRepository.getMinInsuranceFee(), BigDecimal.ZERO),
                defaultIfNull(shipmentRepository.getMaxInsuranceFee(), new BigDecimal("500")),

                countByStatus,
                countByType
        );
    }

    @Transactional(readOnly = true)
    public List<ShipmentMovementDto> getShipmentHistory(Integer shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));
        List<ShipmentMovementDto> history = new ArrayList<>();

        Map<String, String> statusMap = waybillRouteStatusRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        WaybillRouteStatus::getName,
                        WaybillRouteStatus::getName
                ));

        if (shipment.getOriginDeliveryPoint() != null && shipment.getOriginDeliveryPoint().getDeliveryPoint() != null) {
            var deliveryPoint = shipment.getOriginDeliveryPoint().getDeliveryPoint();
            history.add(new ShipmentMovementDto(
                    shipment.getCreatedAt(),
                    deliveryPoint.getCity().getName(),
                    deliveryPoint.getName(),
                    statusMap.getOrDefault("Прийнято до відправлення", "")
            ));
        }

        if (shipment.getShipmentWaybills() != null) {
            shipment.getShipmentWaybills().forEach(sw -> {
                if (sw.getWaybill() != null && sw.getWaybill().getWaybillRoutes() != null) {
                    sw.getWaybill().getWaybillRoutes().forEach(wr -> {
                        if (wr.getRoute() != null
                            && wr.getRoute().getDestinationBranch() != null
                            && wr.getRoute().getDestinationBranch().getDeliveryPoint() != null) {

                            String cityName = wr.getRoute().getDestinationBranch().getDeliveryPoint().getCity().getName();
                            String pointName = wr.getRoute().getDestinationBranch().getDeliveryPoint().getName();

                            if (wr.getArrivedAt() != null) {
                                history.add(new ShipmentMovementDto(
                                        wr.getArrivedAt(),
                                        cityName,
                                        pointName,
                                        statusMap.getOrDefault("Прибуло до відділення", "")
                                ));
                            }

                            if (wr.getDepartedAt() != null) {
                                history.add(new ShipmentMovementDto(
                                        wr.getDepartedAt(),
                                        cityName,
                                        pointName,
                                        statusMap.getOrDefault("Виїхало з відділення", "")
                                ));
                            }
                        }
                    });
                }
            });
        }

        if (shipment.getRouteSheetItems() != null) {
            shipment.getRouteSheetItems().forEach(item -> {
                if (item.getDeliveredAt() != null) {
                    String cityName = "";
                    String fullAddress = "Адреса отримувача";

                    if (shipment.getDestinationAddress() != null
                        && shipment.getDestinationAddress().getAddress() != null) {

                        var addr = shipment.getDestinationAddress().getAddress();
                        var house = addr.getHouse();

                        if (house != null && house.getStreet() != null && house.getStreet().getCity() != null) {
                            cityName = house.getStreet().getCity().getName();
                        }

                        StringBuilder sb = new StringBuilder();

                        if (house != null && house.getStreet() != null) {
                            sb.append(house.getStreet().getName());
                        }
                        if (house != null && house.getNumber() != null) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append("буд. ").append(house.getNumber());
                        }
                        if (addr.getApartmentNumber() != null) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append("кв. ").append(addr.getApartmentNumber());
                        }

                        if (sb.length() > 0) fullAddress = sb.toString();
                    }

                    history.add(new ShipmentMovementDto(
                            item.getDeliveredAt(),
                            cityName,
                            fullAddress,
                            statusMap.getOrDefault("Доставлено кур'єром", "")
                    ));
                }
            });
        }

        if (shipment.getIssuedAt() != null
            && shipment.getDestinationDeliveryPoint() != null
            && shipment.getDestinationDeliveryPoint().getDeliveryPoint() != null) {

            var deliveryPoint = shipment.getDestinationDeliveryPoint().getDeliveryPoint();
            String locationName = deliveryPoint.getName();
            String cityName = deliveryPoint.getCity() != null ? deliveryPoint.getCity().getName() : "";

            history.add(new ShipmentMovementDto(
                    shipment.getIssuedAt(),
                    cityName,
                    locationName,
                    statusMap.getOrDefault("Видано отримувачу", "")
            ));
        }

        return history.stream()
                .sorted(Comparator.comparing(ShipmentMovementDto::time))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShipmentDto> getSuggestedShipments(Integer routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException("Route not found"));

        Integer originPointId = route.getOriginBranch().getDeliveryPoint().getId();
        Integer destPointId = route.getDestinationBranch().getDeliveryPoint().getId();

        List<Shipment> suggested = shipmentRepository.findSuggestedShipments(originPointId, destPointId);

        return suggested.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RestPage<ShipmentDto> findAvailableForSegment(AvailableShipmentsCriteriaDto criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch().getId())
                .orElse(null);

        Trip trip = tripRepository.findById(criteria.tripId())
                .orElseThrow(() -> new EntityNotFoundException("Рейс не знайдено"));

        Integer currentSeq = trip.getWaybillRoutes().stream()
                .filter(wr -> wr.getRoute().getId().equals(criteria.routeId()))
                .map(WaybillRoute::getSequenceNumber)
                .findFirst()
                .orElse(0);

        List<Integer> futureCityIds = trip.getWaybillRoutes().stream()
                .filter(wr -> wr.getSequenceNumber() >= currentSeq)
                .map(wr -> wr.getRoute().getDestinationBranch().getDeliveryPoint().getCity().getId())
                .distinct()
                .toList();

        Specification<Shipment> spec = Specification.where(byBranch(branchId, user.id()))
                .and((root, query, cb) -> {
                    query.distinct(true);

                    var statusPredicate = root.join("shipmentStatus").get("id").in(List.of(1, 2, 4));

                    var destCityJoin = root.join("destinationDeliveryPoint").join("deliveryPoint").join("city");
                    var destinationPredicate = destCityJoin.get("id").in(futureCityIds);

                    if (criteria.trackingNumber() != null && !criteria.trackingNumber().isBlank()) {
                        var searchPredicate = cb.like(cb.lower(root.get("trackingNumber")),
                                "%" + criteria.trackingNumber().toLowerCase() + "%");
                        return cb.and(statusPredicate, destinationPredicate, searchPredicate);
                    }

                    return cb.and(statusPredicate, destinationPredicate);
                });

        return new RestPage<>(shipmentRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    public List<RouteListShipmentDto> getAvailableForRouteList() {
        return shipmentRepository.findAvailableForRouteList().stream()
                .map(shipmentMapper::toRouteListDto)
                .toList();
    }


    private void saveBridgeLocations(Shipment s, RouteLocationDto origin, RouteLocationDto dest) {
        if ("ADDRESS".equalsIgnoreCase(origin.type())) {
            Address address = getOrCreateAddress(origin.streetId(), origin.houseNumber(), origin.apartmentNumber());

            ShipmentOriginAddress soa = new ShipmentOriginAddress();
            soa.setShipment(s);
            soa.setAddress(address);
            originAddressRepository.save(soa);
        } else {
            ShipmentOriginDeliveryPoint op = new ShipmentOriginDeliveryPoint();
            op.setShipment(s);
            op.setDeliveryPoint(deliveryPointRepository.getReferenceById(origin.deliveryPointId()));
            originDeliveryPointRepository.save(op);
        }

        if ("ADDRESS".equalsIgnoreCase(dest.type())) {
            Address address = getOrCreateAddress(dest.streetId(), dest.houseNumber(), dest.apartmentNumber());

            ShipmentDestinationAddress sda = new ShipmentDestinationAddress();
            sda.setShipment(s);
            sda.setAddress(address);
            destinationAddressRepository.save(sda);
        } else {
            ShipmentDestinationDeliveryPoint dp = new ShipmentDestinationDeliveryPoint();
            dp.setShipment(s);
            dp.setDeliveryPoint(deliveryPointRepository.getReferenceById(dest.deliveryPointId()));
            destinationDeliveryPointRepository.save(dp);
        }
    }

    private Address getOrCreateAddress(Integer streetId, String houseNumber, Integer apartmentNumber) {
        AddressHouse house = addressHouseRepository.findByStreetIdAndNumber(streetId, houseNumber)
                .orElseGet(() -> {
                    AddressHouse newHouse = new AddressHouse();
                    newHouse.setStreet(streetRepository.getReferenceById(streetId));
                    newHouse.setNumber(houseNumber);
                    return addressHouseRepository.save(newHouse);
                });

        return addressRepository.findByHouseIdAndApartmentNumber(house.getId(), apartmentNumber)
                .orElseGet(() -> {
                    Address newAddr = new Address();
                    newAddr.setHouse(house);
                    newAddr.setApartmentNumber(apartmentNumber);
                    return addressRepository.save(newAddr);
                });
    }

    private void autoAssignToWaybill(Shipment shipment) {
        try {
            Integer originBranchId = originDeliveryPointRepository.findByShipmentId(shipment.getId())
                    .map(bp -> bp.getDeliveryPoint().getBranch().getId())
                    .orElse(null);

            Integer destCityId = destinationDeliveryPointRepository.findByShipmentId(shipment.getId())
                    .map(bp -> bp.getDeliveryPoint().getCity().getId())
                    .orElse(null);

            if (originBranchId == null || destCityId == null) {
                return;
            }

            List<WaybillRoute> potentialSegments = waybillRouteRepository
                    .findActiveSegmentsForTransitAutoAssign(originBranchId, destCityId);

            if (!potentialSegments.isEmpty()) {
                WaybillRoute segmentToLoad = potentialSegments.get(0);
                Waybill targetWaybill = segmentToLoad.getWaybill();

                ShipmentWaybill sw = new ShipmentWaybill();
                sw.setShipment(shipment);
                sw.setWaybill(targetWaybill);

                int nextSeq = shipmentWaybillRepository.findMaxSequenceNumberByWaybillId(targetWaybill.getId())
                        .map(max -> max + 1)
                        .orElse(1);
                sw.setSequenceNumber(nextSeq);
                shipmentWaybillRepository.save(sw);

                shipment.setShipmentStatus(statusRepository.getReferenceById(4));
                shipmentRepository.save(shipment);

                log.info("AUTO-ASSIGN SUCCESS: Shipment {} added to Waybill #{} (Trip {})",
                        shipment.getTrackingNumber(),
                        targetWaybill.getNumber(),
                        segmentToLoad.getTrip().getNumber());
            }
        } catch (Exception e) {
            log.error("DEBUG-ASSIGN ERROR: Auto-assignment failed: ", e);
        }
    }

    private void deleteExistingLocations(Integer shipmentId) {
        originDeliveryPointRepository.deleteByShipmentId(shipmentId);
        destinationDeliveryPointRepository.deleteByShipmentId(shipmentId);
        originAddressRepository.deleteByShipmentId(shipmentId);
        destinationAddressRepository.deleteByShipmentId(shipmentId);
    }

    private BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value != null ? value : defaultValue;
    }

    private Specification<Shipment> byBranch(Integer branchId, Integer userId) {
        if (branchId == null && userId == null) return null;
        return (root, query, cb) -> {
            query.distinct(true);

            var originSubquery = query.subquery(Integer.class);
            var originRoot = originSubquery.from(ShipmentOriginDeliveryPoint.class);
            originSubquery.select(originRoot.get("shipment").get("id"))
                    .where(cb.equal(originRoot.get("deliveryPoint").get("branch").get("id"), branchId));

            var destSubquery = query.subquery(Integer.class);
            var destRoot = destSubquery.from(ShipmentDestinationDeliveryPoint.class);
            destSubquery.select(destRoot.get("shipment").get("id"))
                    .where(cb.equal(destRoot.get("deliveryPoint").get("branch").get("id"), branchId));

            return cb.or(
                    root.get("id").in(originSubquery),
                    root.get("id").in(destSubquery),
                    cb.equal(root.get("createdBy").get("id"), userId)
            );
        };
    }
}
