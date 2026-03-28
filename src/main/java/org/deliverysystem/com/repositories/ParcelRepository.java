package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Parcel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Integer>, JpaSpecificationExecutor<Parcel> {
    @Query("SELECT p FROM Parcel p WHERE NOT EXISTS (SELECT s FROM Shipment s WHERE s.parcel = p)")
    Page<Parcel> findUnshipped(Pageable pageable);

    @Query("SELECT MIN(p.actualWeight) FROM Parcel p")
    BigDecimal findMinWeight();

    @Query("SELECT MAX(p.actualWeight) FROM Parcel p")
    BigDecimal findMaxWeight();

    @Query("SELECT MIN(p.declaredValue) FROM Parcel p")
    BigDecimal findMinDeclaredValue();

    @Query("SELECT MAX(p.declaredValue) FROM Parcel p")
    BigDecimal findMaxDeclaredValue();
}
