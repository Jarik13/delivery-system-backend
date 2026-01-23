package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.enums.Role;

@Entity
@Table(name = "couriers")
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "courier_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "courier_last_name")),
        @AttributeOverride(name = "middleName", column = @Column(name = "courier_middle_name")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "courier_phone_number"))
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Courier extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courier_id")
    private Integer id;

    @Override
    public Role getRole() {
        return Role.COURIER;
    }
}
