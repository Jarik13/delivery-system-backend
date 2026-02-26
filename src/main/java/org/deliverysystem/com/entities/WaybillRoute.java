package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "waybill_routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaybillRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waybill_route_id")
    private Integer id;

    @Column(name = "trip_sequence_number")
    private Integer sequenceNumber;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;

    @Column(name = "departed_at")
    private LocalDateTime departedAt;

    @ManyToOne
    @JoinColumn(name = "waybill_route_status_id")
    private WaybillRouteStatus status;

    @ManyToOne
    @JoinColumn(name = "waybill_id")
    private Waybill waybill;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}