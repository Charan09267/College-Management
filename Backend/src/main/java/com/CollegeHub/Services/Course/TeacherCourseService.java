package com.CollegeHub.Services.Course;


import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.CourseRepository;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherCourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseCreationResponse createCourse(CourseCreationRequest request) {

        Users teacher = userRepository.findById(request.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        Course course = new Course();

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCreatedBy(teacher);
        course.setCreatedAt(LocalDateTime.now());
        course.setIsActive(true);

        courseRepository.save(course);

        return new CourseCreationResponse(
                "Course has been created successfully"
        );
    }

    public List<Course> getCoursesCreatedByTeacher(Long teacherId) {

        return courseRepository.findByCreatedBy_Id(teacherId);
    }

    public void deleteCourse(Long courseId, String teacherEmail) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        boolean isCourseOwner = course.getCreatedBy()
                .getEmail()
                .equals(teacherEmail);

        if (!isCourseOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not authorized to delete this course"
            );
        }

        courseRepository.delete(course);
    }
}

