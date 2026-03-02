package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "waybill_route_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaybillRouteStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waybill_route_status_id")
    private Integer id;

    @Column(name = "waybill_route_status_name")
    private String name;
}