package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "box_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoxType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_type_id")
    private Integer id;

    @Column(name = "box_type_name")
    private String name;
}
