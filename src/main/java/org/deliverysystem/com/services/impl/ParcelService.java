package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.parcels.ParcelDto;
import org.deliverysystem.com.dtos.parcels.ParcelStatisticsDto;
import org.deliverysystem.com.dtos.search.ParcelSearchCriteria;
import org.deliverysystem.com.entities.Parcel;
import org.deliverysystem.com.entities.StorageCondition;
import org.deliverysystem.com.mappers.ParcelMapper;
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
    private final ParcelTypeRepository parcelTypeRepository;
    private final StorageConditionRepository storageConditionRepository;

    public ParcelService(ParcelRepository repository, ParcelMapper mapper,
                         ParcelTypeRepository parcelTypeRepository,
                         StorageConditionRepository storageConditionRepository) {
        super(mapper, repository);
        this.parcelRepository = repository;
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
    @Cacheable(value = "parcelPages", key = "{#criteria, #pageable}", condition = "#pageable.pageNumber < 5")
    public RestPage<ParcelDto> findAll(ParcelSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            Page<ParcelDto> result = parcelRepository.findAll(pageable).map(mapper::toDto);
            return new RestPage<>(result);
        }

        Specification<Parcel> spec = Specification.where(SpecificationUtils.<Parcel>iLike("contentDescription", criteria.name()))
                .and(SpecificationUtils.equal("parcelType.id", criteria.parcelTypeId()))
                .and(SpecificationUtils.gte("actualWeight", criteria.weightMin()))
                .and(SpecificationUtils.lte("actualWeight", criteria.weightMax()))
                .and(SpecificationUtils.gte("declaredValue", criteria.declaredValueMin()))
                .and(SpecificationUtils.lte("declaredValue", criteria.declaredValueMax()));

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
}