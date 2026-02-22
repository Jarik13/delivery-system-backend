package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.JoinType;
import org.deliverysystem.com.dtos.trips.CreateTripDto;
import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.search.TripSearchCriteria;
import org.deliverysystem.com.dtos.trips.TripSegmentDto;
import org.deliverysystem.com.dtos.trips.WaypointInputDto;
import org.deliverysystem.com.entities.Branch;
import org.deliverysystem.com.entities.Route;
import org.deliverysystem.com.entities.Trip;
import org.deliverysystem.com.entities.WaybillRoute;
import org.deliverysystem.com.mappers.TripMapper;
import org.deliverysystem.com.repositories.*;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class TripService extends AbstractBaseService<Trip, TripDto, Integer> {
    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BranchRepository branchRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final TripStatusRepository tripStatusRepository;
    private final WaybillRouteRepository waybillRouteRepository;

    public TripService(TripRepository repository, TripMapper mapper,
                       RouteRepository routeRepository,
                       BranchRepository branchRepository,
                       DriverRepository driverRepository,
                       VehicleRepository vehicleRepository,
                       TripStatusRepository tripStatusRepository,
                       WaybillRouteRepository waybillRouteRepository) {
        super(mapper, repository);
        this.tripRepository = repository;
        this.routeRepository = routeRepository;
        this.branchRepository = branchRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.tripStatusRepository = tripStatusRepository;
        this.waybillRouteRepository = waybillRouteRepository;
    }

    @Transactional
    @CacheEvict(value = "tripPages", allEntries = true)
    public TripDto createTrip(CreateTripDto dto) {
        Trip trip = new Trip();
        trip.setDriver(driverRepository.getReferenceById(dto.driverId()));
        trip.setVehicle(vehicleRepository.getReferenceById(dto.vehicleId()));
        trip.setScheduledDepartureTime(dto.scheduledDepartureTime());
        trip.setScheduledArrivalTime(dto.scheduledArrivalTime());
        trip.setStatus(tripStatusRepository.getReferenceById(1));

        Trip saved = tripRepository.save(trip);

        if (dto.waypoints() != null && dto.waypoints().size() >= 2) {
            List<WaypointInputDto> sorted = dto.waypoints().stream()
                    .sorted(Comparator.comparing(WaypointInputDto::sequenceNumber))
                    .toList();

            for (int i = 0; i < sorted.size() - 1; i++) {
                Integer fromCityId = sorted.get(i).cityId();
                Integer toCityId = sorted.get(i + 1).cityId();

                Branch originBranch = branchRepository.findFirstByDeliveryPointCityId(fromCityId)
                        .orElseThrow(() -> new EntityNotFoundException("Не знайдено відділення в місті id=" + fromCityId));

                Branch destBranch = branchRepository.findFirstByDeliveryPointCityId(toCityId)
                        .orElseThrow(() -> new EntityNotFoundException("Не знайдено відділення в місті id=" + toCityId));

                Route route = routeRepository
                        .findByOriginBranchIdAndDestinationBranchId(originBranch.getId(), destBranch.getId())
                        .orElseGet(() -> {
                            Route newRoute = new Route();
                            newRoute.setOriginBranch(originBranch);
                            newRoute.setDestinationBranch(destBranch);
                            newRoute.setNeedSorting(false);
                            return routeRepository.save(newRoute);
                        });

                WaybillRoute waybillRoute = new WaybillRoute();
                waybillRoute.setTrip(saved);
                waybillRoute.setRoute(route);
                waybillRoute.setSequenceNumber(i + 1);
                waybillRouteRepository.save(waybillRoute);
            }
        }

        return mapper.toDto(saved);
    }

    @Override
    @CacheEvict(value = "tripPages", allEntries = true)
    public TripDto update(Integer id, TripDto dto) {
        return super.update(id, dto);
    }

    @Override
    @CacheEvict(value = "tripPages", allEntries = true)
    public void delete(Integer id) {
        super.delete(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tripPages", key = "{#criteria, #pageable}", condition = "#pageable.pageNumber < 10")
    public RestPage<TripDto> findAll(TripSearchCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            Page<TripDto> result = tripRepository.findAll(pageable).map(mapper::toDto);
            return new RestPage<>(result);
        }

        Specification<Trip> spec = Specification.where(
                        SpecificationUtils.<Trip>iLike("number", criteria.tripNumber())
                )
                .and(SpecificationUtils.equal("status.id", criteria.tripStatusId()))
                .and(SpecificationUtils.equal("driver.id", criteria.driverId()))
                .and(SpecificationUtils.equal("vehicle.id", criteria.vehicleId()))
                .and(originCitySpec(criteria.originCity()))
                .and(destinationCitySpec(criteria.destinationCity()))
                .and(anyCitySpec(criteria.anyCity()))
                .and(SpecificationUtils.gte("scheduledDepartureTime", criteria.scheduledDepartureFrom()))
                .and(SpecificationUtils.lte("scheduledDepartureTime", criteria.scheduledDepartureTo()))
                .and(SpecificationUtils.gte("actualDepartureTime", criteria.actualDepartureFrom()))
                .and(SpecificationUtils.lte("actualDepartureTime", criteria.actualDepartureTo()))
                .and(SpecificationUtils.gte("scheduledArrivalTime", criteria.scheduledArrivalFrom()))
                .and(SpecificationUtils.lte("scheduledArrivalTime", criteria.scheduledArrivalTo()))
                .and(SpecificationUtils.gte("actualArrivalTime", criteria.actualArrivalFrom()))
                .and(SpecificationUtils.lte("actualArrivalTime", criteria.actualArrivalTo()));

        Page<TripDto> result = tripRepository.findAll(spec, pageable).map(mapper::toDto);
        return new RestPage<>(result);
    }

    @Transactional(readOnly = true)
    public List<TripSegmentDto> getSegments(Integer tripId) {
        Trip trip = repository.findById(tripId).orElseThrow(() -> new EntityNotFoundException("Рейс не знайдено: " + tripId));

        if (trip.getWaybillRoutes() == null) return List.of();

        return trip.getWaybillRoutes().stream()
                .sorted(Comparator.comparing(wr -> wr.getSequenceNumber() != null ? wr.getSequenceNumber() : 0))
                .map(wr -> {
                    Route route = wr.getRoute();
                    String originCity = null;
                    String destCity = null;
                    Double distance = null;

                    try {
                        originCity = route.getOriginBranch().getDeliveryPoint().getCity().getName();
                    } catch (Exception ignored) {
                    }

                    try {
                        destCity = route.getDestinationBranch().getDeliveryPoint().getCity().getName();
                    } catch (Exception ignored) {
                    }

                    try {
                        distance = Double.valueOf(route.getDistanceKm() != null ? route.getDistanceKm() : null);
                    } catch (Exception ignored) {
                    }

                    boolean hasWaybill = waybillRouteRepository.existsByTripIdAndRouteIdAndWaybillIsNotNull(tripId, route.getId());

                    return new TripSegmentDto(
                            route.getId(),
                            wr.getSequenceNumber(),
                            originCity,
                            destCity,
                            distance,
                            hasWaybill
                    );
                })
                .toList();
    }

    private Specification<Trip> originCitySpec(String cityName) {
        if (cityName == null || cityName.isBlank()) return (r, q, cb) -> null;
        return (root, query, cb) -> {
            query.distinct(true);
            var routes = root.join("waybillRoutes", JoinType.LEFT);
            var city = routes.join("route", JoinType.LEFT)
                    .join("originBranch", JoinType.LEFT)
                    .join("deliveryPoint", JoinType.LEFT)
                    .join("city", JoinType.LEFT);
            return cb.like(cb.lower(city.get("name")), "%" + cityName.toLowerCase() + "%");
        };
    }

    private Specification<Trip> destinationCitySpec(String cityName) {
        if (cityName == null || cityName.isBlank()) return (r, q, cb) -> null;
        return (root, query, cb) -> {
            query.distinct(true);
            var routes = root.join("waybillRoutes", JoinType.LEFT);
            var city = routes.join("route", JoinType.LEFT)
                    .join("destinationBranch", JoinType.LEFT)
                    .join("deliveryPoint", JoinType.LEFT)
                    .join("city", JoinType.LEFT);
            return cb.like(cb.lower(city.get("name")), "%" + cityName.toLowerCase() + "%");
        };
    }

    private Specification<Trip> anyCitySpec(String cityName) {
        if (cityName == null || cityName.isBlank()) return (r, q, cb) -> null;
        return (root, query, cb) -> {
            query.distinct(true);
            var routes = root.join("waybillRoutes", JoinType.LEFT);
            var route = routes.join("route", JoinType.LEFT);
            var originCity = route.join("originBranch", JoinType.LEFT)
                    .join("deliveryPoint", JoinType.LEFT)
                    .join("city", JoinType.LEFT);
            var destCity = route.join("destinationBranch", JoinType.LEFT)
                    .join("deliveryPoint", JoinType.LEFT)
                    .join("city", JoinType.LEFT);
            String pattern = "%" + cityName.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(originCity.get("name")), pattern),
                    cb.like(cb.lower(destCity.get("name")), pattern)
            );
        };
    }
}