package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "postomats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Postomat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postomat_id")
    private Integer id;

    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "postomat_code", unique = true)
    private String code;

    @Column(name = "postomat_cells_count")
    private Integer cellsCount;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_point_id", unique = true)
    private DeliveryPoint deliveryPoint;
}
