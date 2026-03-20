package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentOriginAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentOriginAddressRepository extends JpaRepository<ShipmentOriginAddress, Integer> {
    @Modifying
    @Query("DELETE FROM ShipmentOriginAddress s WHERE s.shipment.id = :shipmentId")
    void deleteByShipmentId(@Param("shipmentId") Integer shipmentId);
}