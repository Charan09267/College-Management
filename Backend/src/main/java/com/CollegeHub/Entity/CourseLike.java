package com.CollegeHub.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//@Entity
//@Table(name = "course_likes")

//public class CourseLike {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "course_id")
//    private Course course;
//
//    @ManyToOne
//    @JoinColumn(name = "student_id")
//    private Users student;
//
//    private LocalDateTime likedAt;
//}

@Entity
@Table(
        name = "course_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "student_id"})
        }
)
@Getter
@Setter
public class CourseLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Users student;

    private LocalDateTime likedAt;
}


