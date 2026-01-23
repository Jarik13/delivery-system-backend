package org.deliverysystem.com.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "work_time_intervals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_time_interval_id")
    private Integer id;

    @Column(name = "work_time_interval_start_time")
    private LocalTime startTime;

    @Column(name = "work_time_interval_end_time")
    private LocalTime endTime;
}
