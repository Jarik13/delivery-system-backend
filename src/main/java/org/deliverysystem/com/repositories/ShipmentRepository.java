package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>, JpaSpecificationExecutor<Shipment> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    Page<Shipment> findAllBySenderId(Integer senderId, Pageable pageable);

    @Query("SELECT MIN(s.parcel.actualWeight) FROM Shipment s")
    BigDecimal getMinWeight();

    @Query("SELECT MAX(s.parcel.actualWeight) FROM Shipment s")
    BigDecimal getMaxWeight();


    @Query("SELECT MIN(s.price.total) FROM Shipment s")
    BigDecimal getMinTotalPrice();

    @Query("SELECT MAX(s.price.total) FROM Shipment s")
    BigDecimal getMaxTotalPrice();


    @Query("SELECT MIN(s.price.delivery) FROM Shipment s")
    BigDecimal getMinDeliveryPrice();

    @Query("SELECT MAX(s.price.delivery) FROM Shipment s")
    BigDecimal getMaxDeliveryPrice();


    @Query("SELECT MIN(s.price.weight) FROM Shipment s")
    BigDecimal getMinWeightPrice();

    @Query("SELECT MAX(s.price.weight) FROM Shipment s")
    BigDecimal getMaxWeightPrice();


    @Query("SELECT MIN(s.price.distance) FROM Shipment s")
    BigDecimal getMinDistancePrice();

    @Query("SELECT MAX(s.price.distance) FROM Shipment s")
    BigDecimal getMaxDistancePrice();


    @Query("SELECT MIN(s.price.boxVariant) FROM Shipment s")
    BigDecimal getMinBoxVariantPrice();

    @Query("SELECT MAX(s.price.boxVariant) FROM Shipment s")
    BigDecimal getMaxBoxVariantPrice();


    @Query("SELECT MIN(s.price.specialPackaging) FROM Shipment s")
    BigDecimal getMinSpecialPackagingPrice();

    @Query("SELECT MAX(s.price.specialPackaging) FROM Shipment s")
    BigDecimal getMaxSpecialPackagingPrice();


    @Query("SELECT MIN(s.price.insuranceFee) FROM Shipment s")
    BigDecimal getMinInsuranceFee();

    @Query("SELECT MAX(s.price.insuranceFee) FROM Shipment s")
    BigDecimal getMaxInsuranceFee();
}
