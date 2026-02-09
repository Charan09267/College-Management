package com.CollegeHub.Services.TeacherService;

import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherDetailsResponse;
import com.CollegeHub.Entity.TeacherInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{

    private final TeacherInfoRepo teacherInfoRepo;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<TeacherDetailsResponse> getDetails(String email) {
        System.out.println("Email recieved is:-"+email);
        TeacherDetailsResponse response = new TeacherDetailsResponse();

        Optional<Users> users = userRepository.findByEmail(email);
        System.out.println(users.get().getId()+" is the ID");
        if(!users.isPresent())return ResponseEntity.badRequest().body(null);

        Optional<TeacherInfo> teacherInfo = teacherInfoRepo.findByUser(users.get());
        if(!teacherInfo.isPresent())return ResponseEntity.badRequest().body(null);


        response.setDepartment(teacherInfo.get().getDepartment());
        response.setTeacherId(teacherInfo.get().getTeacherId());
        response.setEmail(users.get().getEmail());
        response.setId(users.get().getId());
        response.setRole(users.get().getRole());

        return ResponseEntity.ok(response);
    }
}
