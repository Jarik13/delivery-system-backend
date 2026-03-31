package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentDestinationDeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentDestinationDeliveryPointRepository extends JpaRepository<ShipmentDestinationDeliveryPoint, Integer> {
    @Modifying
    @Query("DELETE FROM ShipmentDestinationDeliveryPoint s WHERE s.shipment.id = :shipmentId")
    void deleteByShipmentId(@Param("shipmentId") Integer shipmentId);

    Optional<ShipmentDestinationDeliveryPoint> findByShipmentId(Integer shipmentId);
}