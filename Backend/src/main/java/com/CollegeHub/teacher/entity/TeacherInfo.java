package com.CollegeHub.teacher.entity;

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
import java.util.ArrayList;
import java.util.List;

@Document(collection = "teacher_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInfo {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long userId;

    private String name;

    @Indexed(unique = true)
    private String teacherId;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Branch department;

    private String designation;

    private List<String> assignedSubjects = new ArrayList<>();

    private List<AssignedClass> assignedClasses = new ArrayList<>();

    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
