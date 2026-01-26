package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ParcelDto;
import org.deliverysystem.com.entities.Parcel;
import org.deliverysystem.com.entities.StorageCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ParcelMapper extends GenericMapper<Parcel, ParcelDto> {
    @Override
    @Mapping(source = "parcelType.id", target = "parcelTypeId")
    @Mapping(source = "parcelType.name", target = "parcelTypeName")
    @Mapping(source = "storageConditions", target = "storageConditionIds", qualifiedByName = "mapConditionsToIds")
    @Mapping(source = "storageConditions", target = "storageConditionNames", qualifiedByName = "mapConditionsToNames")
    ParcelDto toDto(Parcel entity);

    @Override
    @Mapping(source = "parcelTypeId", target = "parcelType.id")
    @Mapping(target = "storageConditions", ignore = true)
    Parcel toEntity(ParcelDto dto);

    @Named("mapConditionsToIds")
    default Set<Integer> mapConditionsToIds(Set<StorageCondition> conditions) {
        if (conditions == null) return null;
        return conditions.stream().map(StorageCondition::getId).collect(Collectors.toSet());
    }

    @Named("mapConditionsToNames")
    default Set<String> mapConditionsToNames(Set<StorageCondition> conditions) {
        if (conditions == null) return null;
        return conditions.stream().map(StorageCondition::getName).collect(Collectors.toSet());
    }
}