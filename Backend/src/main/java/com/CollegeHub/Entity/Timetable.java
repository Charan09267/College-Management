package com.CollegeHub.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "timetable")
@Data
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Users teacher;

    private String branch;
    private String dayOfWeek; // MONDAY, TUESDAY...
    private Integer periodNumber;
    private LocalDateTime createdAt;
}

