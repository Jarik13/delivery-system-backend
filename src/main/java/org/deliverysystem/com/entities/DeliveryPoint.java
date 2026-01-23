package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "delivery_points")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_point_id")
    private Integer id;

    @Column(name = "delivery_point_name")
    private String name;

    @Column(name = "delivery_point_address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
}
