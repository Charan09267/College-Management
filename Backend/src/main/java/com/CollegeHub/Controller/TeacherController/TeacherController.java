package com.CollegeHub.Controller.TeacherController;

import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherDetailsResponse;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Services.TeacherService.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    Logger logger;

    //get the teacher details by email id....(DONE)
    @GetMapping()
    public ResponseEntity<TeacherDetailsResponse> getDetails(@RequestParam String email){
//        System.out.println("Entered the controller...");
        return teacherService.getDetails(email);
    }
}
