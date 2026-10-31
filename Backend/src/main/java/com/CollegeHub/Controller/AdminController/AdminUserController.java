package com.CollegeHub.Controller.AdminController;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoRequest;
import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoResponse;
import com.CollegeHub.DTO.StudentDetailsUpdateDTO.UpdateResponse;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoRequest;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoResponse;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateRequest;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateResponse;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.UserRepository;
import com.CollegeHub.Services.AdminService.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AdminService adminService;


    @PostMapping("users")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request) {
        return adminService.createUser(request);
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage(){
        return new ResponseEntity<>("Welcomme to my web ",HttpStatus.OK);
    }
    @PostMapping("users/student")
    public ResponseEntity<StudentInfoResponse> addStudentInfo(@RequestBody StudentInfoRequest request){
        System.out.println("Entered the controller / student");
        return adminService.addStudentInfo(request);
    }

    @PostMapping("users/teacher")
    public ResponseEntity<TeacherInfoResponse> addTeacherInfo(@RequestBody TeacherInfoRequest request){
        System.out.println("Inside the controller....");
        return adminService.addTeacherInfo(request);
    }

    @GetMapping("users")
    public ResponseEntity<List<Users>> allUers(){
        return adminService.allUers();
    }


    @GetMapping("/users/student/{id}")
    public ResponseEntity<?> getStudentUserById(@PathVariable Long id){
        return adminService.getStudentUserById(id);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UpdateResponse> updateStudent(){
        return null;
    }

}
