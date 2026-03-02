package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.WaybillRouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaybillRouteStatusRepository extends JpaRepository<WaybillRouteStatus, Integer> {
    Optional<WaybillRouteStatus> findByName(String name);
}