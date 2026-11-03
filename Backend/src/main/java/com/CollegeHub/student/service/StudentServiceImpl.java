package com.CollegeHub.student.service;

import com.CollegeHub.student.dto.StudentDetailsDTO.StudentDetailsRes;
import com.CollegeHub.student.entity.StudentInfo;
import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.student.repository.StudentInfoRepo;
import com.CollegeHub.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;

    @Override
    public StudentDetailsRes getStudentDetails(String email) {

        // Find the user from MySQL using the login email
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Fetch the student's profile from MongoDB using MySQL user ID
        StudentInfo student = studentInfoRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Student profile not found"));

        // Map student profile and account information into response DTO
        StudentDetailsRes response = new StudentDetailsRes();

        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        response.setName(student.getName());
        response.setRollNumber(student.getRollNumber());
        response.setBranch(student.getBranch());
        response.setYear( String.valueOf(student.getYear()) );

        return response;
    }
}