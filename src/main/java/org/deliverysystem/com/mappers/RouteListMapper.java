package org.deliverysystem.com.mappers;


import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteSheetItemDto;
import org.deliverysystem.com.entities.RouteList;
import org.deliverysystem.com.entities.RouteSheetItem;
import org.deliverysystem.com.entities.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

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
    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "shipment.trackingNumber", target = "trackingNumber")
    @Mapping(target = "recipientFullName", expression = "java(formatName(item.getShipment().getRecipient().getLastName(), item.getShipment().getRecipient().getFirstName(), item.getShipment().getRecipient().getMiddleName()))")
    @Mapping(source = "shipment.recipient.phoneNumber", target = "recipientPhone")
    @Mapping(source = "shipment", target = "deliveryAddress", qualifiedByName = "mapOriginAddress")
    @Mapping(source = "shipment", target = "destinationAddress", qualifiedByName = "mapDestinationAddress")
    @Mapping(source = "shipment.parcel.actualWeight", target = "weight")
    @Mapping(source = "shipment.price.total", target = "codAmount")
    @Mapping(source = "delivered", target = "isDelivered")
    @Mapping(source = "deliveredAt", target = "deliveredAt")
    @Mapping(source = "shipment.shipmentStatus.name", target = "shipmentStatusName")
    @Mapping(source = "shipment.price.total", target = "totalPrice")
    @Mapping(target = "hasCod", expression = "java(resolveHasCod(item.getShipment()))")
    @Mapping(target = "remainingAmount", expression = "java(resolveRemainingAmount(item.getShipment()))")
    RouteSheetItemDto toItemDto(RouteSheetItem item);

    @Named("mapOriginAddress")
    default String mapOriginAddress(Shipment shipment) {
        if (shipment.getOriginAddress() != null) {
            try {
                var addr = shipment.getOriginAddress();
                var house = addr.getAddress().getHouse();
                var street = house.getStreet();
                var city = street.getCity();
                StringBuilder sb = new StringBuilder();
                sb.append(city.getName()).append(", ");
                sb.append(street.getName()).append(", ");
                sb.append("буд. ").append(house.getNumber());
                if (addr.getAddress().getApartmentNumber() != null)
                    sb.append(", кв. ").append(addr.getAddress().getApartmentNumber());
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }

        if (shipment.getOriginDeliveryPoint() != null) {
            try {
                var dp = shipment.getOriginDeliveryPoint().getDeliveryPoint();
                StringBuilder sb = new StringBuilder();
                if (dp.getCity() != null) sb.append(dp.getCity().getName()).append(", ");
                if (dp.getName() != null) sb.append(dp.getName());
                if (dp.getAddress() != null) sb.append(", ").append(dp.getAddress());
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Named("mapDestinationAddress")
    default String mapDestinationAddress(Shipment shipment) {
        if (shipment.getDestinationAddress() != null) {
            try {
                var addr = shipment.getDestinationAddress();
                var house = addr.getAddress().getHouse();
                var street = house.getStreet();
                var city = street.getCity();
                StringBuilder sb = new StringBuilder();
                sb.append(city.getName()).append(", ");
                sb.append(street.getName()).append(", ");
                sb.append("буд. ").append(house.getNumber());
                if (addr.getAddress().getApartmentNumber() != null)
                    sb.append(", кв. ").append(addr.getAddress().getApartmentNumber());
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }

        if (shipment.getDestinationDeliveryPoint() != null) {
            try {
                var dp = shipment.getDestinationDeliveryPoint().getDeliveryPoint();
                StringBuilder sb = new StringBuilder();
                if (dp.getCity() != null) sb.append(dp.getCity().getName()).append(", ");
                if (dp.getName() != null) sb.append(dp.getName());
                if (dp.getAddress() != null) sb.append(", ").append(dp.getAddress());
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    default String formatName(String last, String first, String middle) {
        StringBuilder sb = new StringBuilder();
        if (last != null) sb.append(last).append(" ");
        if (first != null) sb.append(first).append(" ");
        if (middle != null) sb.append(middle);
        return sb.toString().trim();
    }

    default Boolean resolveHasCod(Shipment shipment) {
        try {
            return shipment.getPayments() == null || shipment.getPayments().isEmpty()
                    ? shipment.getPrice().getTotal().compareTo(BigDecimal.ZERO) > 0
                    : resolveRemainingAmount(shipment).compareTo(BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    default BigDecimal resolveRemainingAmount(Shipment shipment) {
        try {
            BigDecimal total = shipment.getPrice().getTotal();
            BigDecimal paid = shipment.getPayments() == null
                    ? BigDecimal.ZERO
                    : shipment.getPayments().stream()
                    .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal remaining = total.subtract(paid);
            return remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    @Mapping(source = "courierId", target = "courier.id")
    @Mapping(source = "statusId", target = "status.id")
    @Mapping(source = "items", target = "routeSheetItems")
    RouteList toEntity(RouteListDto dto);
}