package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteListRepository extends JpaRepository<RouteList, Integer> {
    Page<RouteList> findAllByCourierId(Integer courierId, Pageable pageable);
}