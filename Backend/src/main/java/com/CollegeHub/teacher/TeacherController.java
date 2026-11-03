package com.CollegeHub.teacher;

import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.recording.entity.ClassRecording;
import com.CollegeHub.recording.service.ClassRecordingService;
import com.CollegeHub.teacher.dto.TeacherDetailsResponse;
import com.CollegeHub.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    private final TeacherService teacherService;
    private final ClassRecordingService classRecordingService;
    private final UserRepository userRepository;

    // Get logged-in teacher's profile
    @GetMapping("/details")
    public ResponseEntity<TeacherDetailsResponse> getTeacherDetails(
            Authentication authentication) {

        return ResponseEntity.ok(
                teacherService.getTeacherDetails(authentication.getName())
        );
    }

    // Upload a class recording
    @PostMapping(value = "/recordings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClassRecording> uploadRecording(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String subject,
            @RequestParam LocalDate classDate,
            @RequestParam Integer periodNumber,
            @RequestParam String branch,
            @RequestParam String year,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) throws IOException {

        Long teacherUserId = getTeacherUserId(authentication);

        ClassRecording recording = classRecordingService.uploadRecording(
                title,
                description,
                subject,
                classDate,
                periodNumber,
                branch,
                year,
                teacherUserId,
                file
        );

        return ResponseEntity.ok(recording);
    }

    // Get recordings uploaded by the logged-in teacher
    @GetMapping("/recordings")
    public ResponseEntity<List<ClassRecording>> getTeacherRecordings(
            Authentication authentication) {

        Long teacherUserId = getTeacherUserId(authentication);

        return ResponseEntity.ok(
                classRecordingService.getRecordingsForTeacher(teacherUserId)
        );
    }

    // Stream teacher's own recording
    @GetMapping("/recordings/{recordingId}/stream")
    public ResponseEntity<Resource> streamRecording(
            @PathVariable String recordingId,
            Authentication authentication) {

        Long teacherUserId = getTeacherUserId(authentication);

        Resource video = classRecordingService.streamTeacherRecording(
                recordingId,
                teacherUserId
        );

        MediaType mediaType = MediaTypeFactory
                .getMediaType(video)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header("Content-Disposition", "inline")
                .body(video);
    }

    // Delete a recording uploaded by the logged-in teacher
    @DeleteMapping("/recordings/{recordingId}")
    public ResponseEntity<Void> deleteRecording(
            @PathVariable String recordingId,
            Authentication authentication) throws IOException {

        Long teacherUserId = getTeacherUserId(authentication);

        classRecordingService.deleteRecording(recordingId, teacherUserId);

        return ResponseEntity.noContent().build();
    }

    // Resolve authenticated teacher's MySQL user ID
    private Long getTeacherUserId(Authentication authentication) {

        Users user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getId();
    }
}