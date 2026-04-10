package org.deliverysystem.com.dtos.trips;

public record WaypointCoordinateDto(
        Integer cityId,
        String cityName,
        Integer districtId,
        Integer regionId,
        Double latitude,
        Double longitude,
        Integer sequenceNumber
) {}