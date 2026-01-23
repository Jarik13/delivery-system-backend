package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    Page<Shipment> findAllBySenderId(Integer senderId, Pageable pageable);
}
