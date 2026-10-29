package com.CollegeHub.Repository;

import com.CollegeHub.Entity.StudentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentInfoRepo
        extends MongoRepository<StudentInfo, String> {

    Optional<StudentInfo> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByRollNumber(String rollNumber);
}
