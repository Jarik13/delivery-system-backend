package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateCustomIntegerNumber;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "route_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "planned_departure_time")
    private LocalDateTime plannedDepartureTime;

    @OneToMany(mappedBy = "routeList", cascade = CascadeType.ALL)
    private List<RouteSheetItem> routeSheetItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "route_list_status_id")
    private RouteListStatus status;
}
