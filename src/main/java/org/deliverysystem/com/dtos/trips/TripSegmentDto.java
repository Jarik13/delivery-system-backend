package org.deliverysystem.com.dtos.trips;

public record TripSegmentDto(
        Integer routeId,
        Integer waybillId,
        Integer sequenceNumber,
        String originCity,
        String destCity,
        Double distance,
        boolean hasWaybill,
        Double originLat,
        Double originLng,
        Double destLat,
        Double destLng
) {}