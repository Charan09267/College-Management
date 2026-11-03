package com.CollegeHub.recording.service;

import com.CollegeHub.recording.entity.ClassRecording;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ClassRecordingService {

    ClassRecording uploadRecording(
            String title,
            String description,
            String subject,
            LocalDate classDate,
            Integer periodNumber,
            String branch,
            Integer year,
            Long teacherUserId,
            MultipartFile file
    ) throws IOException;

    List<ClassRecording> getRecordingsForStudent(Long studentUserId);

    List<ClassRecording> getRecordingsForTeacher(Long teacherUserId);

    Resource streamRecording(String recordingId, Long studentUserId);

    Resource streamTeacherRecording(String recordingId, Long teacherUserId);

    void deleteRecording(String recordingId, Long teacherUserId) throws IOException;
}