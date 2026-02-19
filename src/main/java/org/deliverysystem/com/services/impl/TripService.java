package org.deliverysystem.com.services.impl;

import org.deliverysystem.com.dtos.trips.TripDto;
import org.deliverysystem.com.dtos.search.TripSearchCriteria;
import org.deliverysystem.com.entities.Trip;
import org.deliverysystem.com.mappers.TripMapper;
import org.deliverysystem.com.repositories.TripRepository;
import org.deliverysystem.com.utils.RestPage;
import org.deliverysystem.com.utils.SpecificationUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService extends AbstractBaseService<Trip, TripDto, Integer> {
    private final TripRepository tripRepository;

    public TripService(TripRepository repository, TripMapper mapper) {
        super(mapper, repository);
        this.tripRepository = repository;
    }

    @Override
    @CacheEvict(value = "tripPages", allEntries = true)
    public TripDto create(TripDto dto) {
        return super.create(dto);
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
                        SpecificationUtils.<Trip>iLike("number", String.valueOf(criteria.tripNumber()))
                )
                .and(SpecificationUtils.equal("status.id", criteria.tripStatusId()))
                .and(SpecificationUtils.equal("driver.id", criteria.driverId()))
                .and(SpecificationUtils.equal("vehicle.id", criteria.vehicleId()))
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
}