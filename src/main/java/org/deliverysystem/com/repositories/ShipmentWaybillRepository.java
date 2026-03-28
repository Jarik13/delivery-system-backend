package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentWaybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentWaybillRepository extends JpaRepository<ShipmentWaybill, Integer> {
    @Query("""
                SELECT sw FROM ShipmentWaybill sw
                LEFT JOIN FETCH sw.shipment s
                LEFT JOIN FETCH s.originDeliveryPoint odp
                LEFT JOIN FETCH odp.deliveryPoint odpp
                LEFT JOIN FETCH odpp.city
                LEFT JOIN FETCH s.destinationDeliveryPoint ddp
                LEFT JOIN FETCH ddp.deliveryPoint ddpp
                LEFT JOIN FETCH ddpp.city
                LEFT JOIN FETCH s.payments
                LEFT JOIN FETCH s.parcel
                LEFT JOIN FETCH s.shipmentStatus
                LEFT JOIN FETCH s.shipmentType
                LEFT JOIN FETCH s.sender
                LEFT JOIN FETCH s.recipient
                WHERE sw.waybill.id = :waybillId
                ORDER BY sw.sequenceNumber
            """)
    List<ShipmentWaybill> findByWaybillIdOrderBySequenceNumber(@Param("waybillId") Integer waybillId);

    @Query("SELECT COALESCE(MAX(sw.sequenceNumber), 0) FROM ShipmentWaybill sw WHERE sw.waybill.id = :waybillId")
    Optional<Integer> findMaxSequenceNumberByWaybillId(@Param("waybillId") Integer waybillId);
}
