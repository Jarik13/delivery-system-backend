package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RouteListRepository extends JpaRepository<RouteList, Integer>, JpaSpecificationExecutor<RouteList> {
    @Query("SELECT MIN(rl.totalWeight) FROM RouteList rl")
    BigDecimal getMinTotalWeight();

    @Query("SELECT MAX(rl.totalWeight) FROM RouteList rl")
    BigDecimal getMaxTotalWeight();
}