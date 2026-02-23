package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteListRepository extends JpaRepository<RouteList, Integer>, JpaSpecificationExecutor<RouteList> {
}