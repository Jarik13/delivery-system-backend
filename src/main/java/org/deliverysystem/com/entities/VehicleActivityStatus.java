package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehicle_activity_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleActivityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_activity_status_id")
    private Integer id;

    @Column(name = "vehicle_activity_status_name")
    private String name;
}
