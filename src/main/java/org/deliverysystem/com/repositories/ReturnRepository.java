package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Integer>, JpaSpecificationExecutor<Return> {
    @Query("SELECT MIN(r.refundAmount) FROM Return r")
    BigDecimal getMinRefundAmount();

    @Query("SELECT MAX(r.refundAmount) FROM Return r")
    BigDecimal getMaxRefundAmount();
}
