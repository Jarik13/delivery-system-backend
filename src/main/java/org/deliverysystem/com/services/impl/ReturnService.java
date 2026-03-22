package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.dtos.returns.CreateReturnDto;
import org.deliverysystem.com.dtos.returns.ReturnDto;
import org.deliverysystem.com.dtos.returns.ReturnStatisticsDto;
import org.deliverysystem.com.dtos.search.ReturnSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.mappers.ReturnMapper;
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

@Service
public class ReturnService extends AbstractBaseService<Return, ReturnDto, Integer> {
    private final ReturnRepository returnRepository;
    private final EmployeeRepository employeeRepository;
    private final ShipmentRepository shipmentRepository;
    private final ReturnReasonRepository returnReasonRepository;
    private final ShipmentStatusRepository statusRepository;
    private final ShipmentOriginDeliveryPointRepository originDeliveryPointRepository;
    private final ShipmentDestinationDeliveryPointRepository destinationDeliveryPointRepository;
    private final ShipmentOriginAddressRepository originAddressRepository;
    private final ShipmentDestinationAddressRepository destinationAddressRepository;

    public ReturnService(ReturnRepository repository, ReturnMapper mapper,
                         EmployeeRepository employeeRepository,
                         ShipmentRepository shipmentRepository,
                         ReturnReasonRepository returnReasonRepository,
                         ShipmentStatusRepository statusRepository,
                         ShipmentOriginDeliveryPointRepository originDeliveryPointRepository,
                         ShipmentDestinationDeliveryPointRepository destinationDeliveryPointRepository,
                         ShipmentOriginAddressRepository originAddressRepository,
                         ShipmentDestinationAddressRepository destinationAddressRepository) {
        super(mapper, repository);
        this.returnRepository = repository;
        this.employeeRepository = employeeRepository;
        this.shipmentRepository = shipmentRepository;
        this.returnReasonRepository = returnReasonRepository;
        this.statusRepository = statusRepository;
        this.originDeliveryPointRepository = originDeliveryPointRepository;
        this.destinationDeliveryPointRepository = destinationDeliveryPointRepository;
        this.originAddressRepository = originAddressRepository;
        this.destinationAddressRepository = destinationAddressRepository;
    }

    @Transactional
    @CacheEvict(value = {"returnPages", "returnStatistics", "shipmentPages"}, allEntries = true)
    public ReturnDto create(CreateReturnDto dto) {
        Shipment original = shipmentRepository.findById(dto.shipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Відправлення не знайдено: " + dto.shipmentId()));

        ReturnReason reason = returnReasonRepository.findById(dto.returnReasonId())
                .orElseThrow(() -> new EntityNotFoundException("Причина повернення не знайдена: " + dto.returnReasonId()));

        ShipmentStatus refusedStatus = statusRepository.findByName("Відмова")
                .orElseThrow(() -> new EntityNotFoundException("Статус 'Відмова' не знайдено"));
        original.setShipmentStatus(refusedStatus);
        shipmentRepository.save(original);

        Shipment returnShipment = new Shipment();
        Price returnPrice = getPrice(original);

        returnShipment.setPrice(returnPrice);
        returnShipment.setSender(original.getRecipient());
        returnShipment.setRecipient(original.getSender());
        returnShipment.setSenderPay(!original.getSenderPay());
        returnShipment.setPartiallyPaid(false);
        returnShipment.setShipmentType(original.getShipmentType());
        returnShipment.setParcel(original.getParcel());
        returnShipment.setCreatedBy(original.getCreatedBy());

        ShipmentStatus returnStatus = statusRepository.findByName("Прийнято у відділенні")
                .orElseThrow(() -> new EntityNotFoundException("Статус не знайдено"));
        returnShipment.setShipmentStatus(returnStatus);

        Shipment savedReturn = shipmentRepository.save(returnShipment);

        copyLocationsReversed(original, savedReturn);

        BigDecimal refund = dto.refundAmount() != null
                ? dto.refundAmount()
                : original.getPrice().getTotal();

        Return ret = new Return();
        ret.setShipment(original);
        ret.setReturnReason(reason);
        ret.setRefundAmount(refund);

        return mapper.toDto(returnRepository.save(ret));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "returnPages", key = "{#criteria, #pageable, #user}", condition = "#pageable.pageNumber < 10")
    public RestPage<ReturnDto> findAll(ReturnSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch() != null ? e.getBranch().getId() : null)
                .orElse(null);

        Specification<Return> spec = Specification.where(byBranch(branchId));

        if (criteria != null) {
            spec = spec
                    .and(SpecificationUtils.iLike("trackingNumber", criteria.returnTrackingNumber()))
                    .and(SpecificationUtils.iLike("shipment.trackingNumber", criteria.shipmentTrackingNumber()))
                    .and(SpecificationUtils.in("returnReason.id", criteria.returnReasons()))
                    .and(SpecificationUtils.gte("initiationDate", criteria.initiationDateFrom()))
                    .and(SpecificationUtils.lte("initiationDate", criteria.initiationDateTo()))
                    .and(SpecificationUtils.gte("refundAmount", criteria.refundAmountMin()))
                    .and(SpecificationUtils.lte("refundAmount", criteria.refundAmountMax()));
        }

        return new RestPage<>(returnRepository.findAll(spec, pageable).map(mapper::toDto));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "returnStatistics", key = "'global'")
    public ReturnStatisticsDto getStatistics() {
        return new ReturnStatisticsDto(
                returnRepository.getMinRefundAmount(),
                returnRepository.getMaxRefundAmount()
        );
    }

    private Price getPrice(Shipment original) {
        BigDecimal returnDelivery = original.getPrice().getDelivery().multiply(new BigDecimal("0.7")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal returnDistance = new BigDecimal("25.00");
        BigDecimal returnTotal = returnDelivery
                .add(original.getPrice().getWeight())
                .add(returnDistance)
                .add(original.getPrice().getInsuranceFee());

        Price returnPrice = new Price();
        returnPrice.setDelivery(returnDelivery);
        returnPrice.setWeight(original.getPrice().getWeight());
        returnPrice.setDistance(returnDistance);
        returnPrice.setBoxVariant(BigDecimal.ZERO);
        returnPrice.setSpecialPackaging(BigDecimal.ZERO);
        returnPrice.setInsuranceFee(original.getPrice().getInsuranceFee());
        returnPrice.setTotal(returnTotal);
        return returnPrice;
    }

    private void copyLocationsReversed(Shipment original, Shipment returnShipment) {
        if (original.getOriginDeliveryPoint() != null) {
            ShipmentDestinationDeliveryPoint dest = new ShipmentDestinationDeliveryPoint();
            dest.setShipment(returnShipment);
            dest.setDeliveryPoint(original.getOriginDeliveryPoint().getDeliveryPoint());
            destinationDeliveryPointRepository.save(dest);
        } else if (original.getOriginAddress() != null) {
            ShipmentDestinationAddress dest = new ShipmentDestinationAddress();
            dest.setShipment(returnShipment);
            dest.setAddress(original.getOriginAddress().getAddress());
            destinationAddressRepository.save(dest);
        }

        if (original.getDestinationDeliveryPoint() != null) {
            ShipmentOriginDeliveryPoint orig = new ShipmentOriginDeliveryPoint();
            orig.setShipment(returnShipment);
            orig.setDeliveryPoint(original.getDestinationDeliveryPoint().getDeliveryPoint());
            originDeliveryPointRepository.save(orig);
        } else if (original.getDestinationAddress() != null) {
            ShipmentOriginAddress orig = new ShipmentOriginAddress();
            orig.setShipment(returnShipment);
            orig.setAddress(original.getDestinationAddress().getAddress());
            originAddressRepository.save(orig);
        }
    }

    private Specification<Return> byBranch(Integer branchId) {
        if (branchId == null) return (root, query, cb) -> cb.conjunction();
        return (root, query, cb) -> cb.or(
                cb.equal(
                        root.join("shipment").join("originDeliveryPoint").join("deliveryPoint").join("branch").get("id"),
                        branchId
                ),
                cb.equal(
                        root.join("shipment").join("destinationDeliveryPoint").join("deliveryPoint").join("branch").get("id"),
                        branchId
                )
        );
    }
}
