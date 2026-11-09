package com.CollegeHub.student.repository;

import com.CollegeHub.student.entity.StudentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface StudentInfoRepo
        extends MongoRepository<StudentInfo, String>,
        StudentInfoRepoCustom {

    Optional<StudentInfo> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByRollNumber(String rollNumber);
}
