package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.StorageConditionDto;
import org.deliverysystem.com.entities.StorageCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageConditionMapper extends GenericMapper<StorageCondition, StorageConditionDto> {
    @Override
    StorageConditionDto toDto(StorageCondition entity);

    @Override
    @Mapping(target = "parcels", ignore = true)
    StorageCondition toEntity(StorageConditionDto dto);
}