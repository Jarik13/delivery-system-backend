package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {
    @Query("SELECT MIN(p.amount) FROM Payment p")
    BigDecimal getMinAmount();

    @Query("SELECT MAX(p.amount) FROM Payment p")
    BigDecimal getMaxAmount();
}
