package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "return_reasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_reason_id")
    private Integer id;

    @Column(name = "return_reason_name")
    private String name;
}
