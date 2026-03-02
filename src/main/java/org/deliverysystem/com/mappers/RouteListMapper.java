package org.deliverysystem.com.mappers;


import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteSheetItemDto;
import org.deliverysystem.com.entities.RouteList;
import org.deliverysystem.com.entities.RouteSheetItem;
import org.deliverysystem.com.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RouteListMapper extends GenericMapper<RouteList, RouteListDto> {
    @Override
    @Mapping(source = "courier.id", target = "courierId")
    @Mapping(target = "courierFullName", expression = "java(formatName(entity.getCourier().getLastName(), entity.getCourier().getFirstName(), entity.getCourier().getMiddleName()))")
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.name", target = "statusName")
    @Mapping(source = "routeSheetItems", target = "items")
    RouteListDto toDto(RouteList entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "shipment.trackingNumber", target = "trackingNumber")
    @Mapping(target = "recipientFullName", expression = "java(formatName(item.getShipment().getRecipient().getLastName(), item.getShipment().getRecipient().getFirstName(), item.getShipment().getRecipient().getMiddleName()))")
    @Mapping(source = "shipment.recipient.phoneNumber", target = "recipientPhone")
    @Mapping(source = "shipment", target = "deliveryAddress", qualifiedByName = "mapFullAddress")
    @Mapping(source = "shipment.parcel.actualWeight", target = "weight")
    @Mapping(source = "shipment.price.total", target = "codAmount")
    @Mapping(source = "delivered", target = "isDelivered")
    @Mapping(source = "deliveredAt", target = "deliveredAt")
    RouteSheetItemDto toItemDto(RouteSheetItem item);

    @Named("mapFullAddress")
    default String mapFullAddress(Shipment shipment) {
        if (shipment.getDestinationAddress() == null) return "Самовивіз з відділення";

        var addr = shipment.getDestinationAddress();
        var house = addr.getAddress().getHouse();
        var street = house.getStreet();
        var city = street.getCity();

        StringBuilder sb = new StringBuilder();
        sb.append(city.getName()).append(", ");
        sb.append(street.getName()).append(", ");
        sb.append("буд. ").append(house.getNumber());

        if (addr.getAddress().getApartmentNumber() != null) {
            sb.append(", кв. ").append(addr.getAddress().getApartmentNumber());
        }

        return sb.toString();
    }

    default String formatName(String last, String first, String middle) {
        StringBuilder sb = new StringBuilder();
        if (last != null) sb.append(last).append(" ");
        if (first != null) sb.append(first).append(" ");
        if (middle != null) sb.append(middle);
        return sb.toString().trim();
    }

    @Override
    @Mapping(source = "courierId", target = "courier.id")
    @Mapping(source = "statusId", target = "status.id")
    @Mapping(source = "items", target = "routeSheetItems")
    RouteList toEntity(RouteListDto dto);
}