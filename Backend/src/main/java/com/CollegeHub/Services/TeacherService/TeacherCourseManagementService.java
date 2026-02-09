package com.CollegeHub.Services.TeacherService;

import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.Entity.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TeacherCourseManagementService {

    ResponseEntity<CourseCreationResponse> createCourse(CourseCreationRequest request);

    ResponseEntity<List<Course>> getMyCourses(Long id);

    void deleteCourse(Long courseId , String teacherEmail);
}
