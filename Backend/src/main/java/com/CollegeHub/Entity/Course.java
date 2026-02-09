package com.CollegeHub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;

    private Integer likesCount;
    private Boolean isActive;
    private LocalDateTime createdAt;


    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<CourseLike> likes = new ArrayList<>();
}

