package org.deliverysystem.com.repositories;

import org.deliverysystem.com.dtos.route_lists.RouteListShipmentDto;
import org.deliverysystem.com.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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

    @Query("""
                SELECT new org.deliverysystem.com.dtos.route_lists.RouteListShipmentDto(
                    s.id,
                    s.trackingNumber,
                    CONCAT(r.lastName, ' ', r.firstName, COALESCE(CONCAT(' ', r.middleName), '')),
                    CASE
                        WHEN da IS NOT NULL THEN
                            CONCAT(COALESCE(sc.name, ''), CASE WHEN sc.name IS NOT NULL THEN ', ' ELSE '' END,
                                   COALESCE(st.name, ''), ' ', COALESCE(h.number, ''),
                                   CASE WHEN a.apartmentNumber IS NOT NULL THEN CONCAT(', кв. ', a.apartmentNumber) ELSE '' END)
                        WHEN sddp IS NOT NULL THEN
                            CONCAT(COALESCE(dpc.name, ''), CASE WHEN dpc.name IS NOT NULL THEN ', ' ELSE '' END, COALESCE(dp.name, ''))
                        ELSE ''
                    END,
                    CASE
                        WHEN da IS NOT NULL THEN
                            CONCAT(COALESCE(sc.name, ''), CASE WHEN sc.name IS NOT NULL THEN ', ' ELSE '' END, COALESCE(st.name, ''))
                        WHEN sddp IS NOT NULL THEN
                            CONCAT(COALESCE(dpc.name, ''), ' — Самовивіз')
                        ELSE ''
                    END,
                    p.actualWeight,
                    CASE WHEN LOWER(shipType.name) LIKE '%експрес%' THEN true ELSE false END
                )
                FROM Shipment s
                JOIN s.shipmentStatus ss
                JOIN s.recipient r
                LEFT JOIN s.destinationAddress da
                LEFT JOIN da.address a
                LEFT JOIN a.house h
                LEFT JOIN h.street st
                LEFT JOIN st.city sc
                LEFT JOIN s.destinationDeliveryPoint sddp
                LEFT JOIN sddp.deliveryPoint dp
                LEFT JOIN dp.city dpc
                LEFT JOIN s.shipmentType shipType
                LEFT JOIN s.parcel p
                WHERE ss.id IN (3, 6)
                AND NOT EXISTS (
                    SELECT rsi FROM RouteSheetItem rsi WHERE rsi.shipment.id = s.id
                )
                ORDER BY s.createdAt DESC
            """)
    List<RouteListShipmentDto> findAvailableForRouteList();
}
