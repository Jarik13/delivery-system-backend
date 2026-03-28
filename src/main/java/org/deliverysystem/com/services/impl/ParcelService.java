package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.parcels.ParcelDto;
import org.deliverysystem.com.dtos.parcels.ParcelStatisticsDto;
import org.deliverysystem.com.dtos.search.ParcelSearchCriteria;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.entities.*;
import org.deliverysystem.com.mappers.ParcelMapper;
import org.deliverysystem.com.repositories.EmployeeRepository;
import org.deliverysystem.com.repositories.ParcelRepository;
import org.deliverysystem.com.repositories.ParcelTypeRepository;
import org.deliverysystem.com.repositories.StorageConditionRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class ParcelService extends AbstractBaseService<Parcel, ParcelDto, Integer> {
    private final ParcelRepository parcelRepository;
    private final EmployeeRepository employeeRepository;
    private final ParcelTypeRepository parcelTypeRepository;
    private final StorageConditionRepository storageConditionRepository;

    public ParcelService(ParcelRepository repository, ParcelMapper mapper,
                         EmployeeRepository employeeRepository,
                         ParcelTypeRepository parcelTypeRepository,
                         StorageConditionRepository storageConditionRepository) {
        super(mapper, repository);
        this.parcelRepository = repository;
        this.employeeRepository = employeeRepository;
        this.parcelTypeRepository = parcelTypeRepository;
        this.storageConditionRepository = storageConditionRepository;
    }

    @Override
    @CacheEvict(value = {"parcelStatistics", "parcelPages"}, allEntries = true)
    public ParcelDto create(ParcelDto dto) {
        Parcel entity = mapper.toEntity(dto);
        setRelationships(entity, dto);
        return mapper.toDto(parcelRepository.save(entity));
    }

    @Override
    @CacheEvict(value = {"parcelStatistics", "parcelPages"}, allEntries = true)
    @CachePut(value = "parcels", key = "#id")
    public ParcelDto update(Integer id, ParcelDto dto) {
        if (!parcelRepository.existsById(id)) {
            throw new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND_FOR_UPDATE);
        }
        Parcel entity = mapper.toEntity(dto);
        entity.setId(id);
        setRelationships(entity, dto);
        return mapper.toDto(parcelRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "parcelPages", key = "{#criteria, #pageable, #user.id()}", condition = "#pageable.pageNumber < 5")
    public RestPage<ParcelDto> findAll(ParcelSearchCriteria criteria, Pageable pageable, CurrentUserDto user) {
        Integer branchId = employeeRepository.findById(user.id())
                .map(e -> e.getBranch().getId())
                .orElse(null);

        Specification<Parcel> spec = Specification.where(parcelByBranch(branchId, user.id()));

        if (criteria != null) {
            spec = spec.and(SpecificationUtils.iLike("contentDescription", criteria.name()))
                    .and(SpecificationUtils.in("parcelType.id", criteria.parcelTypes()))
                    .and(SpecificationUtils.gte("actualWeight", criteria.weightMin()))
                    .and(SpecificationUtils.lte("actualWeight", criteria.weightMax()))
                    .and(SpecificationUtils.gte("declaredValue", criteria.declaredValueMin()))
                    .and(SpecificationUtils.lte("declaredValue", criteria.declaredValueMax()));
        }

        Page<ParcelDto> result = parcelRepository.findAll(spec, pageable).map(mapper::toDto);
        return new RestPage<>(result);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "parcelStatistics", key = "'global'")
    public ParcelStatisticsDto getStatistics() {
        BigDecimal minWeight = parcelRepository.findMinWeight();
        BigDecimal maxWeight = parcelRepository.findMaxWeight();
        BigDecimal minValue = parcelRepository.findMinDeclaredValue();
        BigDecimal maxValue = parcelRepository.findMaxDeclaredValue();

        return new ParcelStatisticsDto(
                minWeight != null ? minWeight : BigDecimal.ZERO,
                maxWeight != null ? maxWeight : BigDecimal.valueOf(100),
                minValue != null ? minValue : BigDecimal.ZERO,
                maxValue != null ? maxValue : BigDecimal.valueOf(50000)
        );
    }

    private void setRelationships(Parcel entity, ParcelDto dto) {
        if (dto.parcelTypeId() != null) {
            entity.setParcelType(parcelTypeRepository.findById(dto.parcelTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Parcel type not found")));
        }

        if (dto.storageConditionIds() != null) {
            Set<StorageCondition> conditions = new HashSet<>(storageConditionRepository.findAllById(dto.storageConditionIds()));
            entity.setStorageConditions(conditions);
        }
    }

    private Specification<Parcel> parcelByBranch(Integer branchId, Integer userId) {
        if (branchId == null && userId == null) return null;

        return (root, query, cb) -> {
            query.distinct(true);

            Join<Parcel, Shipment> shipmentJoin = root.join("shipment", JoinType.LEFT);

            var originSubquery = query.subquery(Integer.class);
            var originRoot = originSubquery.from(ShipmentOriginDeliveryPoint.class);
            originSubquery.select(originRoot.get("shipment").get("id"))
                    .where(cb.equal(originRoot.get("deliveryPoint").get("branch").get("id"), branchId));

            var destSubquery = query.subquery(Integer.class);
            var destRoot = destSubquery.from(ShipmentDestinationDeliveryPoint.class);
            destSubquery.select(destRoot.get("shipment").get("id"))
                    .where(cb.equal(destRoot.get("deliveryPoint").get("branch").get("id"), branchId));

            return cb.or(
                    shipmentJoin.get("id").in(originSubquery),
                    shipmentJoin.get("id").in(destSubquery),
                    cb.equal(shipmentJoin.get("createdBy").get("id"), userId),
                    cb.equal(shipmentJoin.get("issuedBy").get("id"), userId)
            );
        };
    }
}