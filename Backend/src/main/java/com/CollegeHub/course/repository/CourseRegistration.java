package com.CollegeHub.course.repository;

import com.CollegeHub.course.entity.CourseLike;
import com.CollegeHub.admin.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRegistration extends JpaRepository<CourseLike , Long> {
    List<CourseLike> findByStudent_Id(Long studentId);

    List<CourseLike> findByStudent(Users student);

    List<CourseLike> findByCourse_Id(Long id);
}
