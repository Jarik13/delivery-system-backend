package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "clients")
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "client_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "client_last_name")),
        @AttributeOverride(name = "middleName", column = @Column(name = "client_middle_name")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "client_phone_number", unique = true))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer id;

    @Column(name = "client_email", unique = true)
    private String email;
}
