package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "box_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_variant_id")
    private Integer id;

    @Column(name = "box_variant_name")
    private String name;

    @Column(name = "box_variant_price")
    private BigDecimal price;

    @Column(name = "box_variant_width")
    private BigDecimal width;

    @Column(name = "box_variant_length")
    private BigDecimal length;

    @Column(name = "box_variant_height")
    private BigDecimal height;

    @ManyToOne
    @JoinColumn(name = "box_type_id")
    private BoxType boxType;
}
