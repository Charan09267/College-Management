package com.CollegeHub.Controller.StudentController;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.DTO.StudentDetailsRes;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Repository.CourseRepository;
import com.CollegeHub.Repository.StudentInfoRepo;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import com.CollegeHub.Services.StudentService.StudentRecordingService;
import com.CollegeHub.Services.StudentService.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final TeacherInfoRepo teacherInfoRepo;
    private final StudentService studentService;
    private final CourseRepository courseRepository;
    private final StudentRecordingService service;

    @GetMapping()
    public ResponseEntity<StudentDetailsRes> getStudentDetails(@RequestParam String email){

        return studentService.getStudentDetails(email);

    }

    @GetMapping("/courses")
    public ResponseEntity<?> getCourses(){
        List<Course> listCourses = courseRepository.findAll();
        return  ResponseEntity.status(HttpStatus.OK).body(listCourses);
    }

    @PostMapping("/courses/enroll")
    public ResponseEntity<?> enrollTheCourse(@RequestBody StudentCourseRegistration request){
        return studentService.enrollTheCourse(request);
    }

    @GetMapping("/courses/enrolled")
    public ResponseEntity<?> getEnrolledCourses(Authentication auth){
        return studentService.getEnrolledCourses(auth);
    }


}
