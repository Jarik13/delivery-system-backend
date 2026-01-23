package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ShipmentDto;
import org.deliverysystem.com.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper extends GenericMapper<Shipment, ShipmentDto> {
    @Override
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "recipient.id", target = "recipientId")
    @Mapping(source = "parcel.id", target = "parcelId")
    @Mapping(source = "shipmentType.id", target = "shipmentTypeId")
    @Mapping(source = "shipmentStatus.id", target = "shipmentStatusId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "price.total", target = "totalPrice")
    ShipmentDto toDto(Shipment entity);

    @Override
    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "recipientId", target = "recipient.id")
    @Mapping(source = "parcelId", target = "parcel.id")
    @Mapping(source = "shipmentTypeId", target = "shipmentType.id")
    @Mapping(source = "shipmentStatusId", target = "shipmentStatus.id")
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "totalPrice", target = "price.total")
    Shipment toEntity(ShipmentDto dto);
}
