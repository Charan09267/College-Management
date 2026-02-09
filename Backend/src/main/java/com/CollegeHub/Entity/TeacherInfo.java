package com.CollegeHub.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "teacher_info")
@Getter
@Setter
public class TeacherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;

    private String name;
    private String department;
    private String teacherId;
    private LocalDateTime createdAt;
}

