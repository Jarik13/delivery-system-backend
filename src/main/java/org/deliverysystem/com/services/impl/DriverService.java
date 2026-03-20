package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.DriverDto;
import org.deliverysystem.com.entities.Driver;
import org.deliverysystem.com.mappers.DriverMapper;
import org.deliverysystem.com.repositories.DriverRepository;
import org.deliverysystem.com.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DriverService extends AbstractBaseService<Driver, DriverDto, Integer> {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final TripRepository tripRepository;

    private static final List<String> ACTIVE_TRIP_STATUSES = List.of("Заплановано", "В дорозі", "Затримка");

    public DriverService(DriverRepository repository, DriverMapper mapper, TripRepository tripRepository) {
        super(mapper, repository);
        this.driverRepository = repository;
        this.driverMapper = mapper;
        this.tripRepository = tripRepository;
    }

    public List<DriverDto> findAllWithActiveTripStatus() {
        Set<Integer> activeDriverIds = tripRepository.findDriverIdsWithActiveTrips(ACTIVE_TRIP_STATUSES);

        return driverRepository.findAll().stream()
                .map(driver -> {
                    DriverDto dto = driverMapper.toDto(driver);
                    return new DriverDto(
                            dto.id(),
                            dto.firstName(),
                            dto.lastName(),
                            dto.middleName(),
                            dto.phoneNumber(),
                            dto.licenseNumber(),
                            activeDriverIds.contains(driver.getId())
                    );
                })
                .toList();
    }
}