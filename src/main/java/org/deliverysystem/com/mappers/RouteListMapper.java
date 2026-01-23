package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.RouteListDto;
import org.deliverysystem.com.entities.RouteList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RouteListMapper extends GenericMapper<RouteList, RouteListDto> {
    @Override
    @Mapping(source = "courier.id", target = "courierId")
    RouteListDto toDto(RouteList entity);

    @Override
    @Mapping(source = "courierId", target = "courier.id")
    RouteList toEntity(RouteListDto dto);
}