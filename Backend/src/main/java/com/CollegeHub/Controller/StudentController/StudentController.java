package com.CollegeHub.Controller.StudentController;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.DTO.StudentDetailsDTO.StudentDetailsRes;
import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.CourseRepository;
import com.CollegeHub.Repository.UserRepository;
import com.CollegeHub.Services.StudentService.StudentRecordingService;
import com.CollegeHub.Services.StudentService.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final UserRepository userRepository;
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

    @GetMapping("/recordings")
    public List<ClassRecording> fetchRecordings(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Integer period
    ){
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();


        String email = auth.getName();

        Users user = userRepository.findByEmail(email).orElseThrow();

        return service.getRecordings(user , date , subject , period);
    }

    @GetMapping("/recordings/{id}/video")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable Long id,
            @RequestHeader(value = "Range" , required = false)String range
    )throws IOException {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();
        Users user = userRepository.findByEmail(email).orElseThrow();

        return service.streamVideoById(id , user , range);

    }


}
