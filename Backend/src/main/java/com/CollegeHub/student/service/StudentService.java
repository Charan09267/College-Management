package com.CollegeHub.student.service;

import com.CollegeHub.student.dto.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentDetailsRes;
import com.CollegeHub.course.entity.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface StudentService {

    StudentDetailsRes getStudentDetails(String email);
}