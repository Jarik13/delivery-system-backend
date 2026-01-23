package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.StreetDto;
import org.deliverysystem.com.entities.Street;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StreetMapper extends GenericMapper<Street, StreetDto> {
    @Override
    @Mapping(source = "city.id", target = "cityId")
    StreetDto toDto(Street entity);

    @Override
    @Mapping(source = "cityId", target = "city.id")
    Street toEntity(StreetDto dto);
}