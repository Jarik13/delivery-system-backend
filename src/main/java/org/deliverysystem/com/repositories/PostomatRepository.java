package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Postomat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostomatRepository extends JpaRepository<Postomat, Integer> {
    @Query("SELECT p FROM Postomat p WHERE p.deliveryPoint.city.id = :cityId")
    Page<Postomat> findAllByCityId(@Param("cityId") Integer cityId, Pageable pageable);
}