package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "weight_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeightCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_category_id")
    private Integer id;

    @Column(name = "weight_category_name")
    private String name;

    @Column(name = "max_weight", precision = 8, scale = 2)
    private BigDecimal maxWeight;
}