package com.CollegeHub.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "student_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;

    private String name;

    @Column(unique = true)
    private String rollNumber;

    private String branch;
    private Integer year;
    private LocalDateTime createdAt;
}

