package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUser {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
}