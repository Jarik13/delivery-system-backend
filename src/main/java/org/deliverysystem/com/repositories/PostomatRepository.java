package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Postomat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostomatRepository extends JpaRepository<Postomat, Integer>, JpaSpecificationExecutor<Postomat> {
    @Query("SELECT p FROM Postomat p WHERE p.deliveryPoint.city.id = :cityId")
    Page<Postomat> findAllByCityId(@Param("cityId") Integer cityId, Pageable pageable);

    @Query("SELECT MIN(p.cellsCount) FROM Postomat p")
    Integer getMinCellsCount();

    @Query("SELECT MAX(p.cellsCount) FROM Postomat p")
    Integer getMaxCellsCount();
}