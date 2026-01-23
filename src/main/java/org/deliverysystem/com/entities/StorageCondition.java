package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storage_conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_condition_id")
    private Integer id;

    @Column(name = "storage_condition_name")
    private String name;

    @Column(name = "storage_condition_description")
    private String description;

    @ManyToMany(mappedBy = "storageConditions")
    private Set<Parcel> parcels = new HashSet<>();
}
