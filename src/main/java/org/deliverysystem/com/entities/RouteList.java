package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateCustomIntegerNumber;

import java.math.BigDecimal;

@Entity
@Table(name = "route_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_list_id")
    private Integer id;

    @GenerateCustomIntegerNumber
    @Column(name = "route_list_number", unique = true, updatable = false)
    private Integer number;

    @Column(name = "route_list_total_weight")
    private BigDecimal totalWeight;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "route_list_status_id")
    private RouteListStatus status;
}
