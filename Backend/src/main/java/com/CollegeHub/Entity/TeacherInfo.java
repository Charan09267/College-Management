package com.CollegeHub.Entity;

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

    private String department;

    private String designation;

    private List<String> assignedSubjects = new ArrayList<>();

    private List<AssignedClass> assignedClasses = new ArrayList<>();

    private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
