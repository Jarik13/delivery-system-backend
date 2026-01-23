package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "address_houses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_house_id")
    private Integer id;

    @Column(name = "address_house_number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "street_id")
    private Street street;
}
