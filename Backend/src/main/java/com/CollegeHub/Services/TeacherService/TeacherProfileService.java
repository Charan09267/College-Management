package com.CollegeHub.Services.TeacherService;


import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherDetailsResponse;
import com.CollegeHub.Entity.TeacherInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherProfileService {

    private final TeacherInfoRepo teacherInfoRepo;
    private final UserRepository userRepository;

    public TeacherDetailsResponse getTeacherDetails(String email) {

        Users teacher = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher account not found"));

        TeacherInfo teacherInfo = teacherInfoRepo.findByUserId(teacher.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher profile not found"));

        TeacherDetailsResponse response = new TeacherDetailsResponse();

        response.setId(teacher.getId());
        response.setEmail(teacher.getEmail());
        response.setRole(teacher.getRole());
        response.setTeacherId(teacherInfo.getTeacherId());
        response.setDepartment(teacherInfo.getDepartment());

        return response;
    }
}

