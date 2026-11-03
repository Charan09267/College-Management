package com.CollegeHub.student.controller;

import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.recording.dto.RecordingDetailsResponse;
import com.CollegeHub.recording.entity.ClassRecording;
import com.CollegeHub.recording.service.ClassRecordingService;
import com.CollegeHub.student.dto.StudentDetailsDTO.StudentDetailsRes;
import com.CollegeHub.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;
    private final ClassRecordingService classRecordingService;
    private final UserRepository userRepository;

    // Get logged-in student's profile
    @GetMapping("/details")
    public ResponseEntity<StudentDetailsRes> getStudentDetails(
            Authentication authentication) {

        return ResponseEntity.ok(
                studentService.getStudentDetails(authentication.getName())
        );
    }

    // Get recordings matching the logged-in student's academic year
    @GetMapping("/recordings")
    public ResponseEntity<List<RecordingDetailsResponse>> getStudentRecordings(
            Authentication authentication) {

        Long studentUserId = getStudentUserId(authentication);

        return ResponseEntity.ok(
                classRecordingService.getRecordingsForStudent(studentUserId)
        );
    }

    // Stream a recording after validating the student's academic year
    @GetMapping("/recordings/{recordingId}/stream")
    public ResponseEntity<Resource> streamRecording(
            @PathVariable String recordingId,
            Authentication authentication) {

        Long studentUserId = getStudentUserId(authentication);

        Resource video = classRecordingService.streamRecording(
                recordingId,
                studentUserId
        );

        MediaType mediaType = MediaTypeFactory
                .getMediaType(video)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header("Content-Disposition", "inline")
                .body(video);
    }

    // Resolve the authenticated student's MySQL user ID
    private Long getStudentUserId(Authentication authentication) {

        Users user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getId();
    }
}
