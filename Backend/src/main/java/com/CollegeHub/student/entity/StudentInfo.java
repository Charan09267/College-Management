package com.CollegeHub.student.entity;

import com.CollegeHub.admin.entity.Branch;
import com.CollegeHub.admin.entity.ProfileStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private String firstName;
    private String middleName;
    private String lastName;

    @Indexed(unique = true)
    private String rollNumber;

    private String profileImageUrl;

    private String branch;

    @Enumerated(EnumType.STRING)
    private Branch department;

    private Integer year;

    private String section;

    private String batch;

    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}