package com.CollegeHub.teacher.repository;

import com.CollegeHub.admin.entity.ProfileStatus;
import com.CollegeHub.teacher.entity.TeacherInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherInfoRepo
        extends MongoRepository<TeacherInfo, String> {

    Optional<TeacherInfo> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByTeacherId(String teacherId);

    List<TeacherInfo> findByProfileStatus(ProfileStatus profileStatus);
}
