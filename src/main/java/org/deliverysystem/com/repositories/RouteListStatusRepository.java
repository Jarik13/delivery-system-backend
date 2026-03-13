package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteListStatusRepository extends JpaRepository<RouteListStatus, Integer> {
    Optional<RouteListStatus> findByName(String name);
}
