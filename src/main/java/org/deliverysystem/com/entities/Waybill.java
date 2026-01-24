package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateCustomIntegerNumber;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "waybills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Waybill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waybill_id")
    private Integer id;

    @GenerateCustomIntegerNumber
    @Column(name = "waybill_number", unique = true, updatable = false)
    private Integer number;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "waybill_total_weight")
    private BigDecimal totalWeight;

    @Column(name = "waybill_volume")
    private BigDecimal volume;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;
}
