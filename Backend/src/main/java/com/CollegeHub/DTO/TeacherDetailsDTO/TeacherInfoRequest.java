package com.CollegeHub.DTO.TeacherDetailsDTO;

import com.CollegeHub.Entity.AssignedClass;
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
