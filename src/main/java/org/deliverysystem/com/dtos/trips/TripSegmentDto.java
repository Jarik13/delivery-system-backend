package org.deliverysystem.com.dtos.trips;

public record TripSegmentDto(
        Integer waybillRouteId,
        Integer routeId,
        Integer waybillId,
        Integer sequenceNumber,

        Integer originCityId,
        String originCity,
        Integer originDistrictId,
        Integer originRegionId,

        Integer destCityId,
        String destCity,
        Integer destDistrictId,
        Integer destRegionId,

        Double distance,
        boolean hasWaybill,
        Double originLat,
        Double originLng,
        Double destLat,
        Double destLng,
        boolean isCompleted,
        boolean isDeparted
) {}