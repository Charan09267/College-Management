package com.CollegeHub.teacher.service;


import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.teacher.dto.TeacherDetailsResponse;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;
    private final TeacherInfoRepo teacherInfoRepo;

    @Override
    public TeacherDetailsResponse getTeacherDetails(String email) {

        // Find teacher account from MySQL
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find teacher profile from MongoDB
        TeacherInfo teacher = teacherInfoRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Teacher profile not found"));

        // Map account and profile information into response DTO
        TeacherDetailsResponse response = new TeacherDetailsResponse();

        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        response.setName(teacher.getName());
        response.setDepartment(teacher.getDepartment());
        response.setDesignation(teacher.getDesignation());
        response.setAssignedSubjects(teacher.getAssignedSubjects());

        return response;
    }
}
