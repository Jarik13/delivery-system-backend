package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.AddressDto;
import org.deliverysystem.com.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper extends GenericMapper<Address, AddressDto> {
    @Override
    @Mapping(source = "house.id", target = "houseId")
    AddressDto toDto(Address entity);

    @Override
    @Mapping(source = "houseId", target = "house.id")
    Address toEntity(AddressDto dto);
}