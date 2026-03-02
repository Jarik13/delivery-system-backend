package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "origin_branch_id")
    private Branch originBranch;

    @ManyToOne
    @JoinColumn(name = "destination_branch_id")
    private Branch destinationBranch;

    @Column(name = "is_need_sorting")
    private Boolean needSorting;

    @Column(name = "route_distance_km")
    private Float distanceKm;
}
