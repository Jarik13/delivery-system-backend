package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.FleetBrandDto;
import org.deliverysystem.com.entities.FleetBrand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetBrandMapper extends GenericMapper<FleetBrand, FleetBrandDto> {
}