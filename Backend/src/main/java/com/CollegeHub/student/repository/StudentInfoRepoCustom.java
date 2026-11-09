package com.CollegeHub.student.repository;

import com.CollegeHub.student.entity.StudentInfo;

import java.util.List;

public interface StudentInfoRepoCustom {

    List<StudentInfo> findMatchingStudents(
            String branch,
            Integer year,
            String section,
            String batch
    );
}
