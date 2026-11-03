package com.CollegeHub;

import com.CollegeHub.course.entity.CourseLike;
import com.CollegeHub.course.repository.CourseRegistration;
import com.CollegeHub.course.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringBootBakcendApplicationTests {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CourseRegistration courseRegistration;


	@Test
	void contextLoads() {

		courseRepository.deleteById(1L);

		List<CourseLike> likes = courseRegistration.findByCourse_Id(1L);

		assertTrue(likes.isEmpty());
	}

}
