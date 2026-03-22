package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateReturnTrackingNumber;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "returns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Integer id;

    @GenerateReturnTrackingNumber
    @Column(name = "return_tracking_number", unique = true, updatable = false)
    private String trackingNumber;

    @CreatedDate
    @Column(name = "initiation_date")
    private LocalDateTime initiationDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "return_reason_id")
    private ReturnReason returnReason;
}
