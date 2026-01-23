package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parcels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_id")
    private Integer id;

    @Column(name = "parcel_declared_value")
    private BigDecimal declaredValue;

    @Column(name = "parcel_actual_weight")
    private BigDecimal actualWeight;

    @Column(name = "parcel_content_description")
    private String contentDescription;

    @ManyToOne
    @JoinColumn(name = "parcel_type_id")
    private ParcelType parcelType;

    @ManyToMany
    @JoinTable(
            name = "parcels_storage_conditions",
            joinColumns = @JoinColumn(name = "parcel_id"),
            inverseJoinColumns = @JoinColumn(name = "storage_condition_id")
    )
    private Set<StorageCondition> storageConditions = new HashSet<>();
}
