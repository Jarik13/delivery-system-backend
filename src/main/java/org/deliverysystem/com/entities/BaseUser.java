package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.deliverysystem.com.enums.Role;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseUser {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;

    @Transient
    public abstract Role getRole();
}