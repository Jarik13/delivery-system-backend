package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.ShipmentWaybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentWaybillRepository extends JpaRepository<ShipmentWaybill, Integer> {
    @Query("SELECT MAX(sw.sequenceNumber) FROM ShipmentWaybill sw WHERE sw.waybill.id = ?1")
    Optional<Integer> findMaxSequenceNumberByWaybillId(Integer id);
}
