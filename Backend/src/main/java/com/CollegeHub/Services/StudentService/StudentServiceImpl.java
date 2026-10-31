package com.CollegeHub.Services.StudentService;

import com.CollegeHub.DTO.StudentDetailsDTO.StudentCourseRegistration;
import com.CollegeHub.DTO.StudentDetailsDTO.StudentDetailsRes;
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

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final UserRepository userRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final CourseRegistration courseRegistration;
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<StudentDetailsRes> getStudentDetails(String email) {

        Users user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StudentDetailsRes());
        }

        StudentInfo info = studentInfoRepo.findByUserId(user.getId())
                .orElse(null);

        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StudentDetailsRes());
        }

        StudentDetailsRes response = new StudentDetailsRes();

        response.setBranch(info.getBranch());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setName(info.getName());
        response.setRollNumber(info.getRollNumber());
        response.setYear(String.valueOf(info.getYear()));
        response.setUserId(user.getId());

        return ResponseEntity.ok(response);
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

        Users student = userRepository.findByEmail(email)
                .orElseThrow();

        List<Course> courses = courseRegistration
                .findByStudent_Id(student.getId())
                .stream()
                .map(CourseLike::getCourse)
                .toList();

        return ResponseEntity.ok(courses);
    }
}
