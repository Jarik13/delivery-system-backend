package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    @Query("SELECT b FROM Branch b WHERE b.deliveryPoint.city.id = :cityId")
    Page<Branch> findAllByCityId(@Param("cityId") Integer cityId, Pageable pageable);
}