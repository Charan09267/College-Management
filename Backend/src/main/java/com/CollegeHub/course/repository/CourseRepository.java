package com.CollegeHub.course.repository;

import com.CollegeHub.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long > {
    List<Course> findByCreatedBy_Id(Long userId);
}
