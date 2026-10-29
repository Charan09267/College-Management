package com.CollegeHub.Services.TeacherService.interfaces;

import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.Entity.Course;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TeacherCourseManagementService {

    ResponseEntity<CourseCreationResponse> createCourse(CourseCreationRequest request);

    ResponseEntity<List<Course>> getMyCourses(Long id);

    void deleteCourse(Long courseId , String teacherEmail);
}
