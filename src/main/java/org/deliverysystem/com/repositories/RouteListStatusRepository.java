package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteListStatusRepository extends JpaRepository<RouteListStatus, Integer> {
}
