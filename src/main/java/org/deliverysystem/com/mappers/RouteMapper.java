package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.routes.RouteDto;
import org.deliverysystem.com.entities.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RouteMapper extends GenericMapper<Route, RouteDto> {
    @Override
    @Mapping(source = "originBranch.id", target = "originBranchId")
    @Mapping(source = "originBranch.deliveryPoint.name", target = "originBranchName")
    @Mapping(source = "destinationBranch.id", target = "destinationBranchId")
    @Mapping(source = "destinationBranch.deliveryPoint.name", target = "destinationBranchName")
    @Mapping(source = "needSorting", target = "needSorting")
    @Mapping(source = "originBranch.deliveryPoint.city.name", target = "originCityName")
    @Mapping(source = "destinationBranch.deliveryPoint.city.name", target = "destinationCityName")
    RouteDto toDto(Route entity);

    @Override
    @Mapping(source = "originBranchId", target = "originBranch.id")
    @Mapping(source = "destinationBranchId", target = "destinationBranch.id")
    @Mapping(target = "originBranch.deliveryPoint", ignore = true)
    @Mapping(target = "destinationBranch.deliveryPoint", ignore = true)
    Route toEntity(RouteDto dto);
}