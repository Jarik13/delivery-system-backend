package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.WaybillRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaybillRouteRepository extends JpaRepository<WaybillRoute, Integer> {
    Optional<WaybillRoute> findByTripIdAndSequenceNumber(Integer tripId, Integer sequenceNumber);
}
