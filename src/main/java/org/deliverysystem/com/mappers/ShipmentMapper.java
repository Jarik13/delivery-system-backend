package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.ShipmentDto;
import org.deliverysystem.com.entities.BaseUser;
import org.deliverysystem.com.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "recipient.id", target = "recipientId")
    @Mapping(source = "parcel.id", target = "parcelId")
    @Mapping(source = "shipmentType.id", target = "shipmentTypeId")
    @Mapping(source = "shipmentStatus.id", target = "shipmentStatusId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "senderPay", target = "isSenderPay")
    @Mapping(source = "partiallyPaid", target = "isPartiallyPaid")
    @Mapping(source = "price.delivery", target = "deliveryPrice")
    @Mapping(source = "price.weight", target = "weightPrice")
    @Mapping(source = "price.distance", target = "distancePrice")
    @Mapping(source = "price.boxVariant", target = "boxVariantPrice")
    @Mapping(source = "price.specialPackaging", target = "specialPackagingPrice")
    @Mapping(source = "price.insuranceFee", target = "insuranceFee")
    @Mapping(source = "price.total", target = "totalPrice")
    @Mapping(target = "senderFullName", source = "sender", qualifiedByName = "toFullName")
    @Mapping(target = "recipientFullName", source = "recipient", qualifiedByName = "toFullName")
    @Mapping(target = "createdByFullName", source = "createdBy", qualifiedByName = "toFullName")
    @Mapping(source = "parcel.contentDescription", target = "parcelDescription")
    @Mapping(source = "shipmentType.name", target = "shipmentTypeName")
    @Mapping(source = "shipmentStatus.name", target = "shipmentStatusName")
    ShipmentDto toDto(Shipment entity);

    @Mapping(source = "senderId", target = "sender.id")
    @Mapping(source = "recipientId", target = "recipient.id")
    @Mapping(source = "parcelId", target = "parcel.id")
    @Mapping(source = "shipmentTypeId", target = "shipmentType.id")
    @Mapping(source = "shipmentStatusId", target = "shipmentStatus.id")
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "isSenderPay", target = "senderPay")
    @Mapping(source = "isPartiallyPaid", target = "partiallyPaid")
    @Mapping(source = "deliveryPrice", target = "price.delivery")
    @Mapping(source = "weightPrice", target = "price.weight")
    @Mapping(source = "distancePrice", target = "price.distance")
    @Mapping(source = "boxVariantPrice", target = "price.boxVariant")
    @Mapping(source = "specialPackagingPrice", target = "price.specialPackaging")
    @Mapping(source = "insuranceFee", target = "price.insuranceFee")
    @Mapping(source = "totalPrice", target = "price.total")
    Shipment toEntity(ShipmentDto dto);

    @Named("toFullName")
    default String toFullName(BaseUser user) {
        if (user == null) return null;
        return (user.getLastName() + " " + user.getFirstName() + " " + (user.getMiddleName() != null ? user.getMiddleName() : "")).trim();
    }
}