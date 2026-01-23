package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "fleets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fleet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fleet_id")
    private Integer id;

    @Column(name = "fleet_engine_capacity")
    private BigDecimal engineCapacity;

    @Column(name = "fleet_max_speed")
    private Integer maxSpeed;

    @Column(name = "fleet_load_capacity")
    private BigDecimal loadCapacity;

    @Column(name = "fleet_cargo_volume")
    private BigDecimal cargoVolume;

    @ManyToOne
    @JoinColumn(name = "fleet_brand_id")
    private FleetBrand brand;

    @ManyToOne
    @JoinColumn(name = "fleet_fuel_type_id")
    private FleetFuelType fuelType;

    @ManyToOne
    @JoinColumn(name = "fleet_body_type_id")
    private FleetBodyType bodyType;

    @ManyToOne
    @JoinColumn(name = "fleet_transmission_type_id")
    private FleetTransmissionType transmissionType;

    @ManyToOne
    @JoinColumn(name = "fleet_drive_type_id")
    private FleetDriveType driveType;
}
