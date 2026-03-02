package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.WaybillRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaybillRouteRepository extends JpaRepository<WaybillRoute, Long> {
    boolean existsByTripIdAndRouteIdAndWaybillIsNotNull(Integer tripId, Integer routeId);

    @Query("SELECT wr.waybill.id FROM WaybillRoute wr WHERE wr.trip.id = :tripId AND wr.route.id = :routeId AND wr.waybill IS NOT NULL")
    Optional<Integer> findWaybillIdByTripIdAndRouteId(@Param("tripId") Integer tripId, @Param("routeId") Integer routeId);
}
