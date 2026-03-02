package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer>, JpaSpecificationExecutor<Route> {
    Page<Route> findAllByOriginBranchId(Integer originBranchId, Pageable pageable);
    Optional<Route> findByOriginBranchIdAndDestinationBranchId(Integer originBranchId, Integer destinationBranchId);

    @Query("SELECT MIN(r.distanceKm) FROM Route r")
    Float findMinDistanceKm();

    @Query("SELECT MAX(r.distanceKm) FROM Route r")
    Float findMaxDistanceKm();
}