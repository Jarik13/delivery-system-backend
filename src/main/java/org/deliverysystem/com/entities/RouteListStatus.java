package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "route_list_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteListStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_list_status_id")
    private Integer id;

    @Column(name = "route_list_status_name")
    private String name;
}
