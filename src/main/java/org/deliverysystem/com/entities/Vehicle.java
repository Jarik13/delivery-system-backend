package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer id;

    @Column(name = "vehicle_license_plate", unique = true)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "vehicle_activity_status_id")
    private VehicleActivityStatus activityStatus;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;
}
