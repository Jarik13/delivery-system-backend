package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class, ReturnMapper.class})
public interface ShipmentMapper extends GenericMapper<Shipment, ShipmentDto> {
    @Override
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
    @Mapping(source = "parcel.actualWeight", target = "actualWeight")
    @Mapping(source = "shipmentType.name", target = "shipmentTypeName")
    @Mapping(source = "shipmentStatus.name", target = "shipmentStatusName")
    @Mapping(target = "originLocationName", source = "entity", qualifiedByName = "resolveOriginName")
    @Mapping(target = "destinationLocationName", source = "entity", qualifiedByName = "resolveDestinationName")
    @Mapping(source = "payments", target = "payments")
    @Mapping(source = "returns", target = "returns")
    @Mapping(target = "totalPaidAmount", source = "entity", qualifiedByName = "calculateTotalPaid")
    @Mapping(target = "remainingAmount", source = "entity", qualifiedByName = "calculateRemaining")
    @Mapping(target = "isFullyPaid", source = "entity", qualifiedByName = "calculateIsFullyPaid")
    ShipmentDto toDto(Shipment entity);

    @Override
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
    @Mapping(target = "issuedBy", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "originDeliveryPoint", ignore = true)
    @Mapping(target = "destinationDeliveryPoint", ignore = true)
    @Mapping(target = "originAddress", ignore = true)
    @Mapping(target = "destinationAddress", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "returns", ignore = true)
    Shipment toEntity(ShipmentDto dto);

    @Named("calculateTotalPaid")
    default BigDecimal calculateTotalPaid(Shipment entity) {
        if (entity.getPayments() == null || entity.getPayments().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return entity.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateRemaining")
    default BigDecimal calculateRemaining(Shipment entity) {
        BigDecimal totalPaid = calculateTotalPaid(entity);
        BigDecimal totalPrice = (entity.getPrice() != null && entity.getPrice().getTotal() != null)
                ? entity.getPrice().getTotal() : BigDecimal.ZERO;
        return totalPrice.subtract(totalPaid);
    }

    @Named("calculateIsFullyPaid")
    default Boolean calculateIsFullyPaid(Shipment entity) {
        BigDecimal totalPaid = calculateTotalPaid(entity);
        BigDecimal totalPrice = (entity.getPrice() != null && entity.getPrice().getTotal() != null)
                ? entity.getPrice().getTotal() : BigDecimal.ZERO;

        if (totalPrice.compareTo(BigDecimal.ZERO) == 0) return true;

        return totalPaid.compareTo(totalPrice) >= 0;
    }

    @Named("resolveOriginName")
    default String resolveOriginName(Shipment shipment) {
        if (shipment.getOriginDeliveryPoint() != null && shipment.getOriginDeliveryPoint().getDeliveryPoint() != null) {
            return shipment.getOriginDeliveryPoint().getDeliveryPoint().getName();
        }
        if (shipment.getOriginAddress() != null) {
            return formatAddress(shipment.getOriginAddress().getAddress());
        }
        return "Не вказано";
    }

    @Named("resolveDestinationName")
    default String resolveDestinationName(Shipment shipment) {
        if (shipment.getDestinationDeliveryPoint() != null && shipment.getDestinationDeliveryPoint().getDeliveryPoint() != null) {
            return shipment.getDestinationDeliveryPoint().getDeliveryPoint().getName();
        }
        if (shipment.getDestinationAddress() != null) {
            return formatAddress(shipment.getDestinationAddress().getAddress());
        }
        return "Не вказано";
    }

    default String formatAddress(Address addr) {
        if (addr == null || addr.getHouse() == null || addr.getHouse().getStreet() == null) {
            return "Адреса неповна";
        }
        String street = addr.getHouse().getStreet().getName();
        String houseNum = addr.getHouse().getNumber();
        Integer apartment = addr.getApartmentNumber();

        StringBuilder sb = new StringBuilder();
        sb.append(street).append(", буд. ").append(houseNum);

        if (apartment != null && apartment > 0) {
            sb.append(", кв. ").append(apartment);
        }

        return sb.toString();
    }

    @Named("toFullName")
    default String toFullName(BaseUser user) {
        if (user == null) return "Невідомо";
        StringBuilder fullName = new StringBuilder();
        if (user.getLastName() != null) fullName.append(user.getLastName()).append(" ");
        if (user.getFirstName() != null) fullName.append(user.getFirstName()).append(" ");
        if (user.getMiddleName() != null) fullName.append(user.getMiddleName());
        return fullName.toString().trim();
    }
}