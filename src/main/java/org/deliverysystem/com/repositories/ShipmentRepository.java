package org.deliverysystem.com.repositories;

import org.deliverysystem.com.entities.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer>, JpaSpecificationExecutor<Shipment> {
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

    @Query("SELECT s.shipmentStatus.id, COUNT(s) FROM Shipment s GROUP BY s.shipmentStatus.id")
    List<Object[]> countGroupByStatus();

    @Query("SELECT s.shipmentType.id, COUNT(s) FROM Shipment s GROUP BY s.shipmentType.id")
    List<Object[]> countGroupByType();

    @Query(value = """
            SELECT s.* FROM shipments s
            JOIN shipment_origin_delivery_points sodp ON sodp.shipment_id = s.shipment_id
            JOIN shipment_destination_delivery_points sddp ON sddp.shipment_id = s.shipment_id
            WHERE sodp.delivery_point_id = :originPointId
            AND sddp.delivery_point_id = :destPointId
            AND s.shipment_status_id IN (1, 2, 4)
            AND s.issued_at IS NULL
            AND NOT EXISTS (
                SELECT 1 FROM shipment_waybills sw WHERE sw.shipment_id = s.shipment_id
            )
            """, nativeQuery = true)
    List<Shipment> findSuggestedShipments(Integer originPointId, Integer destPointId);

    @Query(value = """
                SELECT s.* FROM shipments s
                JOIN shipment_statuses ss ON ss.shipment_status_id = s.shipment_status_id
                WHERE ss.shipment_status_name IN (N'Прибув у відділення', N'Прийнято у відділенні')
                AND NOT EXISTS (
                    SELECT 1 FROM route_sheet_items rsi WHERE rsi.shipment_id = s.shipment_id
                )
                ORDER BY s.created_at DESC
            """, nativeQuery = true)
    List<Shipment> findAvailableForRouteList();
}
