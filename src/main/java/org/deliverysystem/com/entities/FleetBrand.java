package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "fleet_brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FleetBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fleet_brand_id")
    private Integer id;

    @Column(name = "fleet_brand_name")
    private String name;
}
