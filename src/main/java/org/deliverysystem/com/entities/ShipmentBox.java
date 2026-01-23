package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "shipment_boxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_box_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "shipment_id", unique = true)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "box_variant_id")
    private BoxVariant boxVariant;
}
