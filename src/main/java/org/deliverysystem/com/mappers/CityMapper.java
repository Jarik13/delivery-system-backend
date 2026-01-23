package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.CityDto;
import org.deliverysystem.com.entities.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper extends GenericMapper<City, CityDto> {
    @Override
    @Mapping(source = "district.id", target = "districtId")
    CityDto toDto(City entity);

    @Override
    @Mapping(source = "districtId", target = "district.id")
    City toEntity(CityDto dto);
}