package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateCustomIntegerNumber;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Integer id;

    @GenerateCustomIntegerNumber
    @Column(name = "trip_number", unique = true, updatable = false)
    private Integer number;

    @Column(name = "trip_scheduled_departure_time")
    private LocalDateTime scheduledDepartureTime;

    @Column(name = "trip_actual_departure_time")
    private LocalDateTime actualDepartureTime;

    @Column(name = "trip_actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    @ManyToOne
    @JoinColumn(name = "trip_status_id")
    private TripStatus status;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
