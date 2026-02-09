package com.CollegeHub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "class_recordings")
@Getter
@Setter
public class ClassRecording {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String subject;

    private LocalDate classDate;
    private Integer periodNumber;
    private String branch;

    @JsonIgnore
    private String videoPath; // relative path

    private Integer year;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private Users uploadedBy;

    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
}
