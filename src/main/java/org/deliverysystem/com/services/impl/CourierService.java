package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.CourierDto;
import org.deliverysystem.com.entities.Courier;
import org.deliverysystem.com.mappers.CourierMapper;
import org.deliverysystem.com.repositories.CourierRepository;
import org.deliverysystem.com.repositories.RouteListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CourierService extends AbstractBaseService<Courier, CourierDto, Integer> {
    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;
    private final RouteListRepository routeListRepository;

    private static final List<String> ACTIVE_ROUTE_LIST_STATUSES = List.of("Сформовано", "Видано кур'єру", "У процесі доставки");

    public CourierService(CourierRepository repository, CourierMapper mapper, RouteListRepository routeListRepository) {
        super(mapper, repository);
        this.courierRepository = repository;
        this.courierMapper = mapper;
        this.routeListRepository = routeListRepository;
    }

    public List<CourierDto> findAllWithActiveRouteListStatus() {
        Set<Integer> activeCourierIds = routeListRepository.findCourierIdsWithActiveRouteLists(ACTIVE_ROUTE_LIST_STATUSES);

        return courierRepository.findAll().stream()
                .map(courier -> {
                    CourierDto dto = courierMapper.toDto(courier);
                    return new CourierDto(
                            dto.id(),
                            dto.firstName(),
                            dto.lastName(),
                            dto.middleName(),
                            dto.phoneNumber(),
                            activeCourierIds.contains(courier.getId())
                    );
                })
                .toList();
    }
}