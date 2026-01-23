package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Integer> {
    Optional<Waybill> findByNumber(Integer waybillNumber);
}