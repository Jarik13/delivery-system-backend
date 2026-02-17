package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.PostomatDto;
import org.deliverysystem.com.entities.Postomat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostomatMapper extends GenericMapper<Postomat, PostomatDto> {
    @Override
    @Mapping(source = "deliveryPoint.name", target = "name")
    @Mapping(source = "deliveryPoint.address", target = "address")
    @Mapping(source = "deliveryPoint.id", target = "deliveryPointId")
    @Mapping(source = "deliveryPoint.city.id", target = "cityId")
    @Mapping(source = "deliveryPoint.city.name", target = "cityName")
    PostomatDto toDto(Postomat entity);

    @Override
    @Mapping(source = "name", target = "deliveryPoint.name")
    @Mapping(source = "address", target = "deliveryPoint.address")
    @Mapping(source = "cityId", target = "deliveryPoint.city.id")
    @Mapping(target = "deliveryPoint.city", ignore = true)
    Postomat toEntity(PostomatDto dto);
}