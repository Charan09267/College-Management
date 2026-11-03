package com.CollegeHub.recording.service;

import com.CollegeHub.exception.ResourceAccessDeniedException;
import com.CollegeHub.exception.ResourceNotFoundException;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClassRecordingServiceImpl implements ClassRecordingService {

    private final ClassRecordingRepository classRecordingRepository;
    private final StudentInfoRepo studentInfoRepo;
    private final VideoStorageService videoStorageService;

    // Upload a video to S3 and save its metadata in MongoDB
    @Override
    public ClassRecording uploadRecording(
            String title,
            String description,
            String subject,
            LocalDate classDate,
            Integer periodNumber,
            String branch,
            Integer year,
            Long teacherUserId,
            MultipartFile file
    ) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Video file cannot be empty");
        }

        if (title == null || title.isBlank()
                || subject == null || subject.isBlank()
                || branch == null || branch.isBlank()
                || year == null
                || teacherUserId == null) {
            throw new IllegalArgumentException("Required recording details are missing");
        }

        // Upload the actual video to AWS S3
        String videoPath = videoStorageService.storeVideo(file);

        try {
            // Create recording metadata
            ClassRecording recording = ClassRecording.builder()
                    .title(title)
                    .description(description)
                    .subject(subject)
                    .classDate(classDate)
                    .periodNumber(periodNumber)
                    .branch(branch)
                    .year(year)
                    .uploadedByUserId(teacherUserId)
                    .videoPath(videoPath)
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            return classRecordingRepository.save(recording);

        } catch (RuntimeException exception) {

            // Remove the S3 object if saving metadata in MongoDB fails
            try {
                videoStorageService.deleteVideo(videoPath);
            } catch (IOException | RuntimeException cleanupException) {
                exception.addSuppressed(cleanupException);
            }

            throw exception;
        }
    }

    // Retrieve recordings belonging to the student's academic year
    @Override
    public List<ClassRecording> getRecordingsForStudent(Long studentUserId) {

        StudentInfo student = studentInfoRepo.findByUserId(studentUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        return classRecordingRepository
                .findByYearAndIsActiveTrue(student.getYear());
    }

    // Retrieve recordings uploaded by the logged-in teacher
    @Override
    public List<ClassRecording> getRecordingsForTeacher(Long teacherUserId) {

        return classRecordingRepository
                .findByUploadedByUserIdAndIsActiveTrue(teacherUserId);
    }

    // Stream a recording after validating the student's academic year
    @Override
    public Resource streamRecording(String recordingId, Long studentUserId) {

        StudentInfo student = studentInfoRepo.findByUserId(studentUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student profile not found"));

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recording not found"));

        // Students can only access recordings belonging to their year
        if (!Objects.equals(recording.getYear(), student.getYear())) {
            throw new ResourceAccessDeniedException(
                    "You are not authorized to access this recording");
        }

        return videoStorageService.loadVideo(recording.getVideoPath());
    }

    // Stream a recording only if it belongs to the logged-in teacher
    @Override
    public Resource streamTeacherRecording(
            String recordingId,
            Long teacherUserId) {

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recording not found"));

        if (!Objects.equals(recording.getUploadedByUserId(), teacherUserId)) {
            throw new ResourceAccessDeniedException(
                    "You are not authorized to access this recording");
        }

        return videoStorageService.loadVideo(recording.getVideoPath());
    }

    // Deactivate the recording and remove its video from S3
    @Override
    public void deleteRecording(
            String recordingId,
            Long teacherUserId
    ) throws IOException {

        ClassRecording recording = classRecordingRepository
                .findByIdAndIsActiveTrue(recordingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recording not found"));

        // Only the uploading teacher can delete the recording
        if (!Objects.equals(recording.getUploadedByUserId(), teacherUserId)) {
            throw new ResourceAccessDeniedException(
                    "You are not authorized to delete this recording");
        }

        // Deactivate first so the recording is no longer accessible
        recording.setIsActive(false);
        classRecordingRepository.save(recording);

        // Delete the actual video from S3
        videoStorageService.deleteVideo(recording.getVideoPath());
    }
}
