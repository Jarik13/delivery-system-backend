package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_sheet_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteSheetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_sheet_item_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "route_list_id")
    private RouteList routeList;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "is_delivered")
    private Boolean delivered;
}
