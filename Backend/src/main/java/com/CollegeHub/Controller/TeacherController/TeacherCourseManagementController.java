package com.CollegeHub.Controller.TeacherController;

import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationRequest;
import com.CollegeHub.DTO.CourseCreationDTO.CourseCreationResponse;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Services.TeacherService.TeacherCourseManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/teacher/courses")
public class TeacherCourseManagementController {
   private  final TeacherCourseManagementService teacherCourseManagementService;

    //creation of course....by teacher...(DONE)
    @PostMapping
    public ResponseEntity<CourseCreationResponse> createCourse(@RequestBody CourseCreationRequest request){
        System.out.println("Entered the controller...of create course");
        return teacherCourseManagementService.createCourse(request);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<Course>> getMyCourses(@PathVariable Long id){
       return teacherCourseManagementService.getMyCourses(id);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long courseId ,
            Principal principal
    ){
        teacherCourseManagementService.deleteCourse(courseId , principal.getName());

        return  ResponseEntity.ok("Course has been deleted successfully...");
    }


}
