package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "delivery_point_id", unique = true)
    private DeliveryPoint deliveryPoint;

    @ManyToOne
    @JoinColumn(name = "branch_type_id")
    private BranchType branchType;
}
