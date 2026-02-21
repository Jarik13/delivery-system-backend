package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Integer>, JpaSpecificationExecutor<Waybill> {
    Optional<Waybill> findByNumber(Integer waybillNumber);

    @Query("SELECT MAX(w.number) FROM Waybill w")
    Optional<Integer> findMaxNumber();
}