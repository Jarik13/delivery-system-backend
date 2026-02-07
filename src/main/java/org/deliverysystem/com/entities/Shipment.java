package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.annotations.GenerateShipmentTrackingNumber;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
