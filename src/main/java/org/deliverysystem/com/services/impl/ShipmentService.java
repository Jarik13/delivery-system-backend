package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.search.ShipmentSearchCriteria;
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.dtos.shipments.ShipmentStatisticsDto;
import org.deliverysystem.com.entities.Shipment;
import org.deliverysystem.com.mappers.ShipmentMapper;
import org.deliverysystem.com.repositories.ShipmentRepository;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ShipmentService extends AbstractBaseService<Shipment, ShipmentDto, Integer> {
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository repo, ShipmentMapper mapper) {
        super(mapper, repo);
        this.shipmentRepository = repo;
    }

    @Transactional(readOnly = true)
    public Page<ShipmentDto> findAll(ShipmentSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return shipmentRepository.findAll(pageable).map(mapper::toDto);
        }

        Specification<Shipment> spec = Specification.where(
                        SpecificationUtils.<Shipment>iLike("trackingNumber", criteria.trackingNumber())
                )
                .and(SpecificationUtils.equal("shipmentStatus.id", criteria.shipmentStatusId()))
                .and(SpecificationUtils.equal("shipmentType.id", criteria.shipmentTypeId()))
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

        return shipmentRepository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ShipmentStatisticsDto getStatistics() {
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
                defaultIfNull(shipmentRepository.getMaxInsuranceFee(), new BigDecimal("500"))
        );
    }

    @Transactional(readOnly = true)
    public ShipmentDto findByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.SHIPMENT_NOT_FOUND_BY_TRACKING + trackingNumber));
    }

    @Transactional(readOnly = true)
    public Page<ShipmentDto> findAllBySenderId(Integer senderId, Pageable pageable) {
        return shipmentRepository.findAllBySenderId(senderId, pageable).map(mapper::toDto);
    }

    private BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value != null ? value : defaultValue;
    }
}
