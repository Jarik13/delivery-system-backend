package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionNumber(String transactionNumber);
    Page<Payment> findAllByShipmentId(Integer shipmentId, Pageable pageable);
}
