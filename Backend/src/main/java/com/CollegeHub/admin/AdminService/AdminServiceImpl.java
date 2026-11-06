package com.CollegeHub.admin.AdminService;

import com.CollegeHub.admin.entity.Branch;
import com.CollegeHub.admin.entity.Role;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentInfoRequest;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentInfoResponse;
import com.CollegeHub.teacher.dto.TeacherInfoRequest;
import com.CollegeHub.teacher.dto.TeacherInfoResponse;
import com.CollegeHub.admin.dto.UserCreationDTO.UserCreateRequest;
import com.CollegeHub.admin.dto.UserCreationDTO.UserCreateResponse;
import com.CollegeHub.student.dto.StudentDetailsResponse;
import com.CollegeHub.admin.entity.ProfileStatus;
import com.CollegeHub.student.entity.StudentInfo;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.student.repository.StudentInfoRepo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;
import com.CollegeHub.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final TeacherInfoRepo teacherInfoRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<UserCreateResponse> createUser(UserCreateRequest request) {

        UserCreateResponse response = new UserCreateResponse();
        response.setEmail(request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            response.setMessage("Email already exists for another user.");
            return ResponseEntity.badRequest().body(response);
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().trim().toUpperCase()));
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        response.setId(user.getId());
        response.setMessage("User created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<StudentInfoResponse> addStudentInfo(
            StudentInfoRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (studentInfoRepo.existsByUserId(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (studentInfoRepo.existsByRollNumber(request.getRollNumber())) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime now = LocalDateTime.now();

        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUserId(user.getId());
        studentInfo.setName(request.getName());
        studentInfo.setRollNumber(request.getRollNumber());
        studentInfo.setProfileImageUrl(request.getProfileImageUrl());
        studentInfo.setBranch(request.getBranch());
        studentInfo.setDepartment( Branch.valueOf( request.getDepartment()) );
        studentInfo.setYear(request.getYear());
        studentInfo.setSection(request.getSection());
        studentInfo.setBatch(request.getBatch());
        studentInfo.setProfileStatus(ProfileStatus.ACTIVE);
        studentInfo.setCreatedAt(now);
        studentInfo.setUpdatedAt(now);

        studentInfoRepo.save(studentInfo);

        StudentInfoResponse response = new StudentInfoResponse();
        response.setName(studentInfo.getName());
        response.setRollNumber(studentInfo.getRollNumber());
        response.setBranch(studentInfo.getBranch());
        response.setYear(studentInfo.getYear());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<TeacherInfoResponse> addTeacherInfo(
            TeacherInfoRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (teacherInfoRepo.existsByUserId(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (teacherInfoRepo.existsByTeacherId(request.getTeacherId())) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime now = LocalDateTime.now();

        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setUserId(user.getId());
        teacherInfo.setName(request.getName());
        teacherInfo.setTeacherId(request.getTeacherId());
        teacherInfo.setProfileImageUrl(request.getProfileImageUrl());
        teacherInfo.setDepartment(Branch.valueOf( request.getDepartment()) );
        teacherInfo.setDesignation(request.getDesignation());

        teacherInfo.setAssignedSubjects(
                request.getAssignedSubjects() == null
                        ? List.of()
                        : request.getAssignedSubjects()
        );

        teacherInfo.setAssignedClasses(
                request.getAssignedClasses() == null
                        ? List.of()
                        : request.getAssignedClasses()
        );

        teacherInfo.setProfileStatus(ProfileStatus.ACTIVE);
        teacherInfo.setCreatedAt(now);
        teacherInfo.setUpdatedAt(now);

        teacherInfoRepo.save(teacherInfo);

        TeacherInfoResponse response = new TeacherInfoResponse(
                teacherInfo.getTeacherId(),
                "Teacher information created successfully."
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<Users>> allUers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Override
    public ResponseEntity<StudentDetailsResponse> getStudentUserById(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        StudentInfo studentInfo = studentInfoRepo.findByUserId(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Student profile not found"));

        StudentDetailsResponse response = new StudentDetailsResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(studentInfo.getName());
        response.setYear(String.valueOf(studentInfo.getYear()));
        response.setRollNumber(studentInfo.getRollNumber());
        response.setBranch(studentInfo.getBranch());

        return ResponseEntity.ok(response);
    }
}

