package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "shipment_destination_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDestinationAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_destination_address_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "shipment_id", unique = true)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
