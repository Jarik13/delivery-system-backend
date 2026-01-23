package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "shipment_origin_delivery_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentOriginDeliveryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_origin_delivery_point_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "shipment_id", unique = true)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "delivery_point_id")
    private DeliveryPoint deliveryPoint;
}
