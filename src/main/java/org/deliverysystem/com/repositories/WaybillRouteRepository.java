package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.WaybillRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaybillRouteRepository extends JpaRepository<WaybillRoute, Integer> {
    Optional<WaybillRoute> findByTripIdAndSequenceNumber(Integer tripId, Integer sequenceNumber);

    @Query("""
                SELECT wr FROM WaybillRoute wr
                JOIN wr.trip t
                JOIN wr.route r
                WHERE r.originBranch.id = :originBranchId
                  AND t.status.id IN (1, 2)
                  AND wr.waybill IS NOT NULL
                  AND EXISTS (
                      SELECT 1 FROM WaybillRoute future_wr
                      JOIN future_wr.route future_r
                      WHERE future_wr.trip.id = t.id
                        AND future_wr.sequenceNumber >= wr.sequenceNumber
                        AND future_r.destinationBranch.deliveryPoint.city.id = :destCityId
                  )
                ORDER BY t.scheduledDepartureTime ASC
            """)
    List<WaybillRoute> findActiveSegmentsForTransitAutoAssign(
            @Param("originBranchId") Integer originBranchId,
            @Param("destCityId") Integer destCityId
    );
}
