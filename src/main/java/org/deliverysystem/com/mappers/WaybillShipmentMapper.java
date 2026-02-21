package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.entities.Client;
import org.deliverysystem.com.entities.Shipment;
import org.deliverysystem.com.entities.ShipmentWaybill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WaybillShipmentMapper {
    @Mapping(source = "shipment.id", target = "id")
    @Mapping(source = "shipment.trackingNumber", target = "trackingNumber")
    @Mapping(source = "shipment.createdAt", target = "createdAt")
    @Mapping(source = "shipment.price.total", target = "totalPrice")
    @Mapping(source = "shipment.parcel.actualWeight", target = "actualWeight")
    @Mapping(source = "shipment.shipmentStatus.name", target = "shipmentStatusName")
    @Mapping(source = "shipment.sender", target = "senderFullName", qualifiedByName = "clientFullName")
    @Mapping(source = "shipment.recipient", target = "recipientFullName", qualifiedByName = "clientFullName")
    @Mapping(target = "originCityName", source = "shipment", qualifiedByName = "resolveOriginCity")
    @Mapping(target = "destinationCityName", source = "shipment", qualifiedByName = "resolveDestinationCity")
    WaybillShipmentDto toDto(ShipmentWaybill shipmentWaybill);

    @Named("clientFullName")
    default String clientFullName(Client client) {
        if (client == null) return "Невідомо";
        StringBuilder sb = new StringBuilder();
        if (client.getLastName() != null) sb.append(client.getLastName()).append(" ");
        if (client.getFirstName() != null) sb.append(client.getFirstName()).append(" ");
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
        } catch (Exception ignored) {
        }
        return null;
    }

    @Named("resolveDestinationCity")
    default String resolveDestinationCity(Shipment shipment) {
        try {
            if (shipment.getDestinationDeliveryPoint() != null)
                return shipment.getDestinationDeliveryPoint().getDeliveryPoint().getCity().getName();
            if (shipment.getDestinationAddress() != null)
                return shipment.getDestinationAddress().getAddress().getHouse().getStreet().getCity().getName();
        } catch (Exception ignored) {
        }
        return null;
    }
}