package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.RouteDto;
import org.deliverysystem.com.entities.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RouteMapper extends GenericMapper<Route, RouteDto> {
    @Override
    @Mapping(source = "originBranch.id", target = "originBranchId")
    @Mapping(source = "destinationBranch.id", target = "destinationBranchId")
    RouteDto toDto(Route entity);

    @Override
    @Mapping(source = "originBranchId", target = "originBranch.id")
    @Mapping(source = "destinationBranchId", target = "destinationBranch.id")
    Route toEntity(RouteDto dto);
}