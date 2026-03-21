package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.RouteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
public interface RouteListRepository extends JpaRepository<RouteList, Integer>, JpaSpecificationExecutor<RouteList> {
    @Query("SELECT MIN(rl.totalWeight) FROM RouteList rl")
    BigDecimal getMinTotalWeight();

    @Query("SELECT MAX(rl.totalWeight) FROM RouteList rl")
    BigDecimal getMaxTotalWeight();

    @Query("SELECT rl.status.id, COUNT(rl) FROM RouteList rl GROUP BY rl.status.id")
    List<Object[]> countGroupByStatus();

    @Query("SELECT rl.courier.id FROM RouteList rl WHERE rl.status.name IN :statuses")
    Set<Integer> findCourierIdsWithActiveRouteLists(@Param("statuses") List<String> statuses);
}