package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "drivers")
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "driver_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "driver_last_name")),
        @AttributeOverride(name = "middleName", column = @Column(name = "driver_middle_name")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "driver_phone_number", unique = true)),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Integer id;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "driver_license_number", unique = true)
    private String licenseNumber;
}