package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "trip_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_status_id")
    private Integer id;

    @Column(name = "trip_status_name")
    private String name;
}
