package org.deliverysystem.com.repositories;

import jakarta.validation.constraints.NotNull;
import org.deliverysystem.com.entities.WaybillRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillRouteRepository extends JpaRepository<WaybillRoute, Long> {
    boolean existsByTripIdAndRouteId(Integer tripId, Integer routeId);
}
