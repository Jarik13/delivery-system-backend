package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "shipment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_type_id")
    private Integer id;

    @Column(name = "shipment_type_name")
    private String name;
}
