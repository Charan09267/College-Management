package com.CollegeHub.recording.service;



import com.CollegeHub.recording.entity.ClassRecording;
import com.CollegeHub.recording.repository.ClassRecordingRepository;
import com.CollegeHub.storage.VideoStorageService;
import com.CollegeHub.student.entity.StudentInfo;
import com.CollegeHub.student.repository.StudentInfoRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassRecordingServiceImpl implements ClassRecordingService {

    private final ClassRecordingRepository classRecordingRepository;
    private final StudentInfoRepo studentInfoRepository;
    private final VideoStorageService videoStorageService;

    @Override
    public ClassRecording uploadRecording(
            String title,
            String description,
            String subject,
            LocalDate classDate,
            Integer periodNumber,
            String branch,
            String year,
            Long teacherUserId,
            MultipartFile file
    ) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be empty");
        }

        // Store the actual video in AWS S3
        String videoPath = videoStorageService.storeVideo(file);

        try {
            // Create recording metadata
            ClassRecording recording = new ClassRecording();

            recording.setTitle(title);
            recording.setDescription(description);
            recording.setSubject(subject);
            recording.setClassDate(classDate);
            recording.setPeriodNumber(periodNumber);
            recording.setBranch(branch);
            recording.setYear(Integer.parseInt(year));
            recording.setUploadedByUserId(teacherUserId);
            recording.setVideoPath(videoPath);
            recording.setIsActive(true);

            return classRecordingRepository.save(recording);

        } catch (RuntimeException exception) {

            // Clean up the uploaded S3 object if MongoDB save fails
            try {
                videoStorageService.deleteVideo(videoPath);
            } catch (IOException cleanupException) {
                exception.addSuppressed(cleanupException);
            }

            throw exception;
        }
    }

    @Override
    public List<ClassRecording> getRecordingsForStudent(Long studentUserId) {

        StudentInfo student = studentInfoRepository.findByUserId(studentUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student profile not found"));

        // Students can only see recordings belonging to their academic year
        return classRecordingRepository
                .findByYearAndIsActiveTrue(student.getYear());
    }

    @Override
    public List<ClassRecording> getRecordingsForTeacher(Long teacherUserId) {

        return classRecordingRepository
                .findByUploadedByUserIdAndIsActiveTrue(teacherUserId);
    }

    @Override
    public Resource streamRecording(String recordingId, Long studentUserId) {

        StudentInfo student = studentInfoRepository.findByUserId(studentUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student profile not found"));

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Recording not found"));

        // Prevent students from accessing recordings from other years
        if (!recording.getYear().equals(student.getYear())) {
            throw new IllegalArgumentException(
                    "You are not authorized to access this recording");
        }

        return videoStorageService.loadVideo(recording.getVideoPath());
    }

    @Override
    public void deleteRecording(String recordingId, Long teacherUserId)
            throws IOException {

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Recording not found"));

        // Only the teacher who uploaded the recording can delete it
        if (!recording.getUploadedByUserId().equals(teacherUserId)) {
            throw new IllegalArgumentException(
                    "You are not authorized to delete this recording");
        }

        videoStorageService.deleteVideo(recording.getVideoPath());

        classRecordingRepository.delete(recording);
    }

    @Override
    public Resource streamTeacherRecording(
            String recordingId,
            Long teacherUserId) {

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Recording not found"));

        if (!recording.getUploadedByUserId().equals(teacherUserId)) {
            throw new IllegalArgumentException(
                    "You are not authorized to access this recording");
        }

        return videoStorageService.loadVideo(recording.getVideoPath());
    }
}
