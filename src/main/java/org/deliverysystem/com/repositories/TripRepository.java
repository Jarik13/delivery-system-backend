package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer>, JpaSpecificationExecutor<Trip> {
    @Query("SELECT DISTINCT t.driver.id FROM Trip t WHERE t.status.name IN :activeStatuses")
    Set<Integer> findDriverIdsWithActiveTrips(@Param("activeStatuses") List<String> activeStatuses);
}