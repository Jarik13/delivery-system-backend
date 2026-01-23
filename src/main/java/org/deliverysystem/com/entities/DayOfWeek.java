package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "days_of_week")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayOfWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "days_of_week_id")
    private Integer id;

    @Column(name = "days_of_week_name")
    private String name;
}
