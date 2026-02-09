package com.CollegeHub.Services.StudentService;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.DTO.StudentDetailsRes;
import com.CollegeHub.Entity.Course;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface StudentService {

    ResponseEntity<StudentDetailsRes> getStudentDetails(String email);

    ResponseEntity<?> enrollTheCourse(StudentCourseRegistration request);

    public ResponseEntity<List<Course>> getEnrolledCourses(Authentication auth);
}
