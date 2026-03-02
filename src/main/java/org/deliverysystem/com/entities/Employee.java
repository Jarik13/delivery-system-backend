package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.deliverysystem.com.enums.Role;

@Entity
@Table(name = "employees")
@AttributeOverrides({
        @AttributeOverride(name = "firstName", column = @Column(name = "employee_first_name")),
        @AttributeOverride(name = "lastName", column = @Column(name = "employee_last_name")),
        @AttributeOverride(name = "middleName", column = @Column(name = "employee_middle_name")),
        @AttributeOverride(name = "phoneNumber", column = @Column(name = "employee_phone_number", unique = true)),
        @AttributeOverride(name = "password", column = @Column(name = "employee_password")),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @Column(name = "employee_email", unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Override
    public Role getRole() {
        return Role.BRANCH_OPERATOR;
    }
}