package com.CollegeHub.Services.StudentService;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.DTO.StudentDetailsRes;
import com.CollegeHub.Entity.Course;
import com.CollegeHub.Entity.CourseLike;
import com.CollegeHub.Entity.StudentInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.CourseRegistration;
import com.CollegeHub.Repository.CourseRepository;
import com.CollegeHub.Repository.StudentInfoRepo;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final CourseRegistration courseRegistration;
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<StudentDetailsRes> getStudentDetails(String email) {

        Optional<Users> user = userRepository.findByEmail(email);
        if(!user.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StudentDetailsRes());

        Optional<StudentInfo> info = studentInfoRepo.findByUser_Id(user.get().getId());

        if(!info.isPresent()){
            System.out.println("Info in 39 th line is executing...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        StudentDetailsRes response = new StudentDetailsRes();
        response.setBranch(info.get().getBranch());
        response.setEmail(user.get().getEmail());
        response.setRole(user.get().getRole());
        response.setName(info.get().getName());
        response.setRollNumber(info.get().getRollNumber());
        response.setYear(String.valueOf(info.get().getYear()));
        response.setUserId(user.get().getId());


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Override
    public ResponseEntity<?> enrollTheCourse(StudentCourseRegistration request) {
        CourseLike course = new CourseLike();

        Optional<Course> course1 =  courseRepository.findById(request.getCourseId());

        Optional<Users> user = userRepository.findById(request.getStudentId());

        if(course1.isPresent()) {
            course.setCourse(course1.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration Error..try again later");
        }


        if(user.isPresent()) {
            course.setStudent(user.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration Error..try again later");
        }

        course.setLikedAt(LocalDateTime.now());


        courseRegistration.save(course);
        int likesCount = course1.get().getLikesCount() == null ? 0 : course1.get().getLikesCount().intValue() ;
        course1.get().setLikesCount(Integer.valueOf(likesCount+1));

        courseRepository.save(course1.get());

        return ResponseEntity.ok("Registration successfull....");

    }

    @Override
    public ResponseEntity<List<Course>> getEnrolledCourses(Authentication auth) {
        String email = auth.getName();
        Users student = userRepository.findByEmail(email).orElseThrow();

        System.out.println("Email :-"+email+" student id :-"+ student.getId());

        List<Course> courses = courseRegistration
                .findByStudent_Id(student.getId())
                .stream()
                .map(CourseLike::getCourse)
                .toList();

        return ResponseEntity.ok(courses);

    }
}
