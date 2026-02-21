package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.entities.Client;
import org.deliverysystem.com.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WaybillShipmentMapper {
    @Mapping(source = "price.total",           target = "totalPrice")
    @Mapping(source = "parcel.actualWeight",   target = "actualWeight")
    @Mapping(source = "shipmentStatus.name",   target = "shipmentStatusName")
    @Mapping(source = "sender",                target = "senderFullName",    qualifiedByName = "clientFullName")
    @Mapping(source = "recipient",             target = "recipientFullName", qualifiedByName = "clientFullName")
    @Mapping(target = "originCityName",        source = "entity", qualifiedByName = "resolveOriginCity")
    @Mapping(target = "destinationCityName",   source = "entity", qualifiedByName = "resolveDestinationCity")
    WaybillShipmentDto toDto(Shipment entity);

    @Named("clientFullName")
    default String clientFullName(Client client) {
        if (client == null) return "Невідомо";
        StringBuilder sb = new StringBuilder();
        if (client.getLastName()   != null) sb.append(client.getLastName()).append(" ");
        if (client.getFirstName()  != null) sb.append(client.getFirstName()).append(" ");
        if (client.getMiddleName() != null) sb.append(client.getMiddleName());
        return sb.toString().trim();
    }

    @Named("resolveOriginCity")
    default String resolveOriginCity(Shipment shipment) {
        try {
            if (shipment.getOriginDeliveryPoint() != null)
                return shipment.getOriginDeliveryPoint().getDeliveryPoint().getCity().getName();
            if (shipment.getOriginAddress() != null)
                return shipment.getOriginAddress().getAddress().getHouse().getStreet().getCity().getName();
        } catch (Exception ignored) {}
        return null;
    }

    @Named("resolveDestinationCity")
    default String resolveDestinationCity(Shipment shipment) {
        try {
            if (shipment.getDestinationDeliveryPoint() != null)
                return shipment.getDestinationDeliveryPoint().getDeliveryPoint().getCity().getName();
            if (shipment.getDestinationAddress() != null)
                return shipment.getDestinationAddress().getAddress().getHouse().getStreet().getCity().getName();
        } catch (Exception ignored) {}
        return null;
    }
}