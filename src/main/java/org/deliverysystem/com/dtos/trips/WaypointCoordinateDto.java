package org.deliverysystem.com.dtos.trips;

public record WaypointCoordinateDto(
        String cityName,
        Double latitude,
        Double longitude,
        Integer sequenceNumber
) {}
