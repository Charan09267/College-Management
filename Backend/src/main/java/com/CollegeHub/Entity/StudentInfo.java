package com.CollegeHub.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long userId;

    private String name;

    @Indexed(unique = true)
    private String rollNumber;

    private String profileImageUrl;

    private String branch;

    private String department;

    private Integer year;

    private String section;

    private String batch;

    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}