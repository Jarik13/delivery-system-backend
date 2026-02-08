package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateShipmentTrackingNumber;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Integer id;

    @GenerateShipmentTrackingNumber
    @Column(name = "shipment_tracking_number", unique = true, updatable = false)
    private String trackingNumber;

    @Embedded
    private Price price;

    @Column(name = "is_sender_pay")
    private Boolean senderPay;

    @Column(name = "is_partially_paid")
    private Boolean partiallyPaid;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @OneToOne
    @JoinColumn(name = "parcel_id", unique = true)
    private Parcel parcel;

    @OneToOne(mappedBy = "shipment")
    private ShipmentOriginDeliveryPoint originDeliveryPoint;

    @OneToOne(mappedBy = "shipment")
    private ShipmentDestinationDeliveryPoint destinationDeliveryPoint;

    @OneToOne(mappedBy = "shipment")
    private ShipmentOriginAddress originAddress;

    @OneToOne(mappedBy = "shipment")
    private ShipmentDestinationAddress destinationAddress;

    @OneToOne(mappedBy = "shipment")
    private ShipmentBox shipmentBox;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<ShipmentWaybill> shipmentWaybills = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<RouteSheetItem> routeSheetItems = new ArrayList<>();

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "issued_by_id")
    private Employee issuedBy;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Client recipient;

    @ManyToOne
    @JoinColumn(name = "shipment_type_id")
    private ShipmentType shipmentType;

    @ManyToOne
    @JoinColumn(name = "shipment_status_id")
    private ShipmentStatus shipmentStatus;
}
