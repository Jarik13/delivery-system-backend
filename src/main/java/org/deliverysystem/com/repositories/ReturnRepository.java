package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Integer>, JpaSpecificationExecutor<Return> {
}
