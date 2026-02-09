package com.CollegeHub.Repository;

import com.CollegeHub.Entity.TeacherInfo;
import com.CollegeHub.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TeacherInfoRepo extends JpaRepository<TeacherInfo , Long> {

    Optional<TeacherInfo> findByTeacherId(String teacherId);

    Optional<TeacherInfo> findByUser(Users user);

    Optional<TeacherInfo> findByUser_Id(Long userId);
}
