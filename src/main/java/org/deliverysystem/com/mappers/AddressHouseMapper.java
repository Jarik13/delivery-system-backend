package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.AddressHouseDto;
import org.deliverysystem.com.entities.AddressHouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressHouseMapper extends GenericMapper<AddressHouse, AddressHouseDto> {
    @Override
    @Mapping(source = "street.id", target = "streetId")
    AddressHouseDto toDto(AddressHouse entity);

    @Override
    @Mapping(source = "streetId", target = "street.id")
    AddressHouse toEntity(AddressHouseDto dto);
}