package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.RegionDto;
import org.deliverysystem.com.entities.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper extends GenericMapper<Region, RegionDto> {
}