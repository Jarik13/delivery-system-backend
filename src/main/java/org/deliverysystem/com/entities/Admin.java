package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "admins")
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "admin_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "admin_last_name")),
        @AttributeOverride(name = "middleName", column = @Column(name = "admin_middle_name")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "admin_phone_number", unique = true)),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer id;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;
}