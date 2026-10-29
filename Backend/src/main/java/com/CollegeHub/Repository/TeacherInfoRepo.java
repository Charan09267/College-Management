package com.CollegeHub.Repository;

import com.CollegeHub.Entity.TeacherInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherInfoRepo
        extends MongoRepository<TeacherInfo, String> {

    Optional<TeacherInfo> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByTeacherId(String teacherId);
}
