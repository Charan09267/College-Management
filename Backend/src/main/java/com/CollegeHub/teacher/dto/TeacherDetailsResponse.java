package com.CollegeHub.teacher.dto;


import lombok.Data;
import java.util.List;

@Data
public class TeacherDetailsResponse {

    private Long userId;

    private String email;

    private String role;

    private String name;

    private String department;

    private String designation;

    private List<String> assignedSubjects;
}