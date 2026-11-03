package com.CollegeHub.teacher.dto;

import com.CollegeHub.teacher.entity.AssignedClass;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherInfoRequest {

    private Long userId;

    private String name;

    private String teacherId;

    private String profileImageUrl;

    private String department;

    private String designation;

    private List<String> assignedSubjects;

    private List<AssignedClass> assignedClasses;
}
