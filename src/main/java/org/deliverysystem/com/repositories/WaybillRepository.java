package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Integer>, JpaSpecificationExecutor<Waybill> {
    Optional<Waybill> findByNumber(Integer waybillNumber);

    @Query("SELECT MAX(w.number) FROM Waybill w")
    Optional<Integer> findMaxNumber();

    @Query("SELECT MIN(w.totalWeight) FROM Waybill w WHERE w.totalWeight IS NOT NULL")
    Optional<BigDecimal> findMinTotalWeight();

    @Query("SELECT MAX(w.totalWeight) FROM Waybill w WHERE w.totalWeight IS NOT NULL")
    Optional<BigDecimal> findMaxTotalWeight();

    @Query("SELECT MIN(w.volume) FROM Waybill w WHERE w.volume IS NOT NULL")
    Optional<BigDecimal> findMinVolume();

    @Query("SELECT MAX(w.volume) FROM Waybill w WHERE w.volume IS NOT NULL")
    Optional<BigDecimal> findMaxVolume();
}