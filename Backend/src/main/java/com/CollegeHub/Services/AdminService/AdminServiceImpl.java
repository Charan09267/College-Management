package com.CollegeHub.Services.AdminService;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoRequest;
import com.CollegeHub.DTO.StudentDetailsDTO.StudentInfoResponse;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoRequest;
import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherInfoResponse;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateRequest;
import com.CollegeHub.DTO.UserCreationDTO.UserCreateResponse;
import com.CollegeHub.DTO.UserInformationDetails.StudentDetailsResponse;
import com.CollegeHub.Entity.StudentInfo;
import com.CollegeHub.Entity.TeacherInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.StudentInfoRepo;
import com.CollegeHub.Repository.TeacherInfoRepo;
import com.CollegeHub.Repository.UserRepository;
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
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final TeacherInfoRepo teacherInfoRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<UserCreateResponse> createUser(UserCreateRequest request) {
        UserCreateResponse userCreateResponse = new UserCreateResponse();

        userCreateResponse.setEmail(request.getEmail());



        System.out.println("Inside the post method...");

        if (userRepository.existsByEmail(request.getEmail())) {
            userCreateResponse.setMessage("Email already exists of the user...");
            return ResponseEntity.badRequest().body(userCreateResponse);
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println("Password is:-"+user.getPassword());
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        userCreateResponse.setId(user.getId());
        userCreateResponse.setMessage("User creation successfull....");


        return ResponseEntity.status(HttpStatus.CREATED).body(userCreateResponse);


    }


    @Override
    public ResponseEntity<StudentInfoResponse> addStudentInfo(StudentInfoRequest request) {
        Users user = userRepository.findById(request.getUserId()).orElseThrow(()->new RuntimeException("Internal server error..???!!!"));

        //setting userDetails....
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUser(user);
        studentInfo.setName(request.getName());
        studentInfo.setBranch(request.getBranch());
        studentInfo.setYear(request.getYear());
        studentInfo.setRollNumber(request.getRollNumber());
        studentInfo.setCreatedAt(LocalDateTime.now());

        studentInfoRepo.save(studentInfo);

        StudentInfoResponse studentInfoResponse = new StudentInfoResponse();
        studentInfoResponse.setName(request.getName());
        studentInfoResponse.setBranch(request.getBranch());
        studentInfoResponse.setRollNumber(request.getRollNumber());


        return ResponseEntity.status(HttpStatus.CREATED).body(studentInfoResponse);
    }

    @Override
    public ResponseEntity<TeacherInfoResponse> addTeacherInfo(TeacherInfoRequest request) {
        Users user = userRepository.findById(request.getUserId()).orElseThrow(()->new UsernameNotFoundException("Error in user id...??"));


        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setTeacherId(request.getTeacherId());
        teacherInfo.setName(request.getName());
        teacherInfo.setUser(user);
        teacherInfo.setCreatedAt(LocalDateTime.now());
        teacherInfo.setDepartment(request.getDepartment());

        teacherInfoRepo.save(teacherInfo);


        TeacherInfoResponse response = new TeacherInfoResponse(request.getTeacherId() , "Teacher's Information has been updated");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<Users>> allUers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @Override
    public ResponseEntity<StudentDetailsResponse> getStudentUserById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User with id not found......"));
        StudentDetailsResponse response = new StudentDetailsResponse();

        response.setEmail(user.getEmail());
        response.setId(id);

        StudentInfo studentInfo = studentInfoRepo.findByUser_Id(id).orElseThrow((()->new RuntimeException("student user with id not found......")));

        response.setName(studentInfo.getName());
        response.setYear(String.valueOf(studentInfo.getYear()));
        response.setRollNumber(studentInfo.getRollNumber());
        response.setBranch(studentInfo.getBranch());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
