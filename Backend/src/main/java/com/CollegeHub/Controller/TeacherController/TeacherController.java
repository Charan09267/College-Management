package com.CollegeHub.Controller.TeacherController;

import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherDetailsResponse;
import com.CollegeHub.Entity.Course;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher")
public class TeacherController {
//    private final TeacherService teacherService;
//    Logger logger;
//
//    @GetMapping()
//    public ResponseEntity<TeacherDetailsResponse> getDetails(@RequestParam String email){
//        return teacherService.getDetails(email);
//    }
//
//
//    private  final TeacherCourseManagementService teacherCourseManagementService;
//
//    //creation of course....by teacher...
//    @PostMapping("/courses")
//    public ResponseEntity<CourseCreationResponse> createCourse(@RequestBody CourseCreationRequest request){
//        return teacherCourseManagementService.createCourse(request);
//    }
//
//    @GetMapping("/courses/{id}")
//    public ResponseEntity<List<Course>> getMyCourses(@PathVariable Long id){
//        return teacherCourseManagementService.getMyCourses(id);
//    }
//
//    @DeleteMapping("/courses/{courseId}")
//    public ResponseEntity<?> deleteCourse(
//            @PathVariable Long courseId ,
//            Principal principal
//    ){
//        teacherCourseManagementService.deleteCourse(courseId , principal.getName());
//
//        return  ResponseEntity.ok("Course has been deleted successfully...");
//    }
}
