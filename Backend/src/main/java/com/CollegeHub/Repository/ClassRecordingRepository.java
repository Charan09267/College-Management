package com.CollegeHub.Repository;

import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClassRecordingRepository extends JpaRepository<ClassRecording , Long> {

    @Query("""
        SELECT cr FROM ClassRecording cr
        WHERE cr.year = :year
          AND cr.branch = :branch
          AND cr.isActive = true
          AND (:date IS NULL OR cr.classDate = :date)
          AND (:subject IS NULL OR cr.subject = :subject)
          AND (:period IS NULL OR cr.periodNumber = :period)
    """)
    List<ClassRecording> searchForStudent(
            @Param("year") Integer year,
            @Param("branch") String branch,
            @Param("date") LocalDate date,
            @Param("subject") String subject,
            @Param("period") Integer period
            );

    @Query("""
        SELECT cr FROM ClassRecording cr
        WHERE cr.uploadedBy = :teacher
          AND cr.isActive = true
          AND (:date IS NULL OR cr.classDate = :date)
          AND (:subject IS NULL OR cr.subject = :subject)
          AND (:period IS NULL OR cr.periodNumber = :period)
    """)
    List<ClassRecording> searchForTeacher(
            @Param("teacher") Users teacher,
            @Param("date") LocalDate date,
            @Param("subject") String subject,
            @Param("period") Integer period
    );
}
