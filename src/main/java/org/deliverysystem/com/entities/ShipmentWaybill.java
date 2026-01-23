package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "shipment_waybills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentWaybill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_waybill_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "waybill_id")
    private Waybill waybill;

    @Column(name = "shipment_sequence_number")
    private Integer sequenceNumber;
}
