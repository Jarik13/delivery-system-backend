package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.RouteListStatusDto;
import org.deliverysystem.com.entities.RouteListStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteListStatusMapper extends GenericMapper<RouteListStatus, RouteListStatusDto> {
}
