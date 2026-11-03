package com.CollegeHub.teacher.dto;


import lombok.Data;

@Data
public class TeacherDetailsResponse {

    private Long id;

    private String email;

    private String role;

    private String teacherId;

    private String department;
}