package com.CollegeHub.Controller.StudentController;

import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.StudentInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import com.CollegeHub.Services.StudentService.StudentRecordingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students/recordings")
@RequiredArgsConstructor
public class StudentRecordigController {

    private final StudentRecordingService service;
    private final StudentInfoRepo studentInfoRepo;
    private final UserRepository userRepository;

    @GetMapping
    public List<ClassRecording> fetchRecordings(
            @RequestParam(required = false)LocalDate date,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Integer period
            ){
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();


        String email = auth.getName();

        Users user = userRepository.findByEmail(email).orElseThrow();

        return service.getRecordings(user , date , subject , period);
    }

    @GetMapping("/{id}/video")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable Long id,
            @RequestHeader(value = "Range" , required = false)String range
    )throws IOException{
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();
        Users user = userRepository.findByEmail(email).orElseThrow();

        return service.streamVideoById(id , user , range);

    }


}
