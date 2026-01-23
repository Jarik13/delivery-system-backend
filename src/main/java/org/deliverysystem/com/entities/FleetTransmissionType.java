package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "fleet_transmission_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FleetTransmissionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fleet_transmission_type_id")
    private Integer id;

    @Column(name = "fleet_transmission_type_name")
    private String name;
}
