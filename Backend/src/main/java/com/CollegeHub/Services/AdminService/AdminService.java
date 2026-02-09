package com.CollegeHub.Services.AdminService;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoRequest;
import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoResponse;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoRequest;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoResponse;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateRequest;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateResponse;
import com.CollegeHub.DTO.UserInformationDetails.StudentDetailsResponse;
import com.CollegeHub.Entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<UserCreateResponse> createUser(UserCreateRequest userCreateRequest);

    ResponseEntity<StudentInfoResponse> addStudentInfo(StudentInfoRequest request);

    ResponseEntity<TeacherInfoResponse> addTeacherInfo(TeacherInfoRequest request);

    ResponseEntity<List<Users>> allUers();

    ResponseEntity<StudentDetailsResponse> getStudentUserById(Long id);

}
