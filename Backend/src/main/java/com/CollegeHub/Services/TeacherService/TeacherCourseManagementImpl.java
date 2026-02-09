package com.CollegeHub.Services.TeacherService;

import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Entity.TeacherInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.CourseRepository;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TeacherCourseManagementImpl implements TeacherCourseManagementService{

    private final TeacherInfoRepo teacherInfoRepo;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<CourseCreationResponse> createCourse(CourseCreationRequest request) {
        System.out.println("Entered the controller...of create course");
        Users teacher = userRepository.findById(request.getId()).orElseThrow(()->new RuntimeException("Teacher with Id not found...."));
        Course courseDetails = new Course();
        courseDetails.setTitle(request.getTitle());
        courseDetails.setCreatedBy(teacher);
        courseDetails.setDescription(request.getDescription());
        courseDetails.setCreatedAt(LocalDateTime.now());
        courseDetails.setIsActive(true);

        courseRepository.save(courseDetails);

        CourseCreationResponse response = new CourseCreationResponse("Course has been created successfully...");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<Course>> getMyCourses(Long id) {
        List<Course> courses = courseRepository.findByCreatedBy_Id(id);

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @Override
    public void deleteCourse(Long courseId, String teacherEmail) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found???"));

        if(!course.getCreatedBy().getEmail().equals(teacherEmail)){
            throw new RuntimeException("You are not allowed to delete this course..!!!");

        }

        courseRepository.delete(course);
    }
}
