package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteSheetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteSheetItemRepository extends JpaRepository<RouteSheetItem, Integer> {
    Optional<RouteSheetItem> findByRouteListIdAndShipmentId(Integer routeListId, Integer shipmentId);
}
