package com.CollegeHub.Repository;

import com.CollegeHub.Entity.StudentInfo;
import com.CollegeHub.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StudentInfoRepo extends JpaRepository<StudentInfo, Long> {
    Optional<StudentInfo> findByUser_Id(Long id);

    Optional<StudentInfo> findByUser(Users user);
}
