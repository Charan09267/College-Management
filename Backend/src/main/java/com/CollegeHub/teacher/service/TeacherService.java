package com.CollegeHub.teacher.service;


import com.CollegeHub.teacher.dto.TeacherDetailsResponse;

public interface TeacherService {

    TeacherDetailsResponse getTeacherDetails(String email);

}
