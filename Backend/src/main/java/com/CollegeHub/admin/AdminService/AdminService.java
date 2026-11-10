package com.CollegeHub.admin.AdminService;

import com.CollegeHub.admin.dto.UserCreateResponse;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentInfoRequest;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentInfoResponse;
import com.CollegeHub.teacher.dto.TeacherInfoRequest;
import com.CollegeHub.teacher.dto.TeacherInfoResponse;
import com.CollegeHub.admin.dto.UserCreateRequest;
import com.CollegeHub.student.dto.StudentDetailsResponse;
import com.CollegeHub.admin.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<UserCreateResponse> createUser(UserCreateRequest userCreateRequest);

    ResponseEntity<StudentInfoResponse> addStudentInfo(StudentInfoRequest request);

    ResponseEntity<TeacherInfoResponse> addTeacherInfo(TeacherInfoRequest request);

    ResponseEntity<List<Users>> allUers();

    ResponseEntity<StudentDetailsResponse> getStudentUserById(Long id);

}
