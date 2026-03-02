package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateCustomIntegerNumber;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waybills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Waybill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waybill_id")
    private Integer id;

    @GenerateCustomIntegerNumber
    @Column(name = "waybill_number", unique = true, updatable = false)
    private Integer number;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "waybill_total_weight")
    private BigDecimal totalWeight;

    @Column(name = "waybill_volume")
    private BigDecimal volume;

    @OneToMany(mappedBy = "waybill", cascade = CascadeType.ALL)
    private List<ShipmentWaybill> shipmentWaybills = new ArrayList<>();

    @OneToMany(mappedBy = "waybill", cascade = CascadeType.ALL)
    private List<WaybillRoute> waybillRoutes = new ArrayList<>();

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;
}
