package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.DistrictDto;
import org.deliverysystem.com.entities.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistrictMapper extends GenericMapper<District, DistrictDto> {
    @Override
    @Mapping(source = "region.id", target = "regionId")
    DistrictDto toDto(District entity);

    @Override
    @Mapping(source = "regionId", target = "region.id")
    District toEntity(DistrictDto dto);
}