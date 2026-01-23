package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ParcelDto;
import org.deliverysystem.com.entities.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelMapper extends GenericMapper<Parcel, ParcelDto> {
    @Override
    @Mapping(source = "parcelType.id", target = "parcelTypeId")
    ParcelDto toDto(Parcel entity);

    @Override
    @Mapping(source = "parcelTypeId", target = "parcelType.id")
    Parcel toEntity(ParcelDto dto);
}
