package com.CollegeHub.Controller.TeacherController;

import com.CollegeHub.DTO.ClassRecordings.ClassRecordingRequestDTO;
import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.ClassRecordingRepository;
import com.CollegeHub.Repository.UserRepository;
import com.CollegeHub.Services.TeacherService.ClassRecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher/recordings")
public class ClassRecordingController {

    private final ClassRecordingService service;
    private final ClassRecordingRepository classRecordingRepository;

    @Autowired
    private UserRepository userRepository;

    public ClassRecordingController(ClassRecordingService service, ClassRecordingRepository classRecordingRepository){
        this.service = service;
        this.classRecordingRepository = classRecordingRepository;
    }


    //upload the class recordings for teahcer
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadClassRecording(
            @RequestPart("video")MultipartFile video,
            @RequestPart("data")ClassRecordingRequestDTO request

            ) throws IOException{

        if(video.isEmpty()){
            return ResponseEntity.badRequest().body("Video file is missing...???");
        }
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();

        String email = auth.getName();

        Users user = userRepository.findByEmail(email).orElseThrow();


        ClassRecording saved = service.upload(video,request , user);

        System.out.println(user.getEmail());

        Map<String, Object> response = Map.of(
                "id", saved.getId(),
                "title", saved.getTitle(),
                "classDate", saved.getClassDate(),
                "periodNumber", saved.getPeriodNumber(),
                "branch", saved.getBranch(),
                "year", saved.getYear(),
                "videoPath", saved.getVideoPath(),
                "message", "Upload successful"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //get the recordings for the current teacher
    @GetMapping
    public List<ClassRecording> fetchRecordings(
            @RequestParam(required = false)LocalDate date,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Integer period
            ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        Users teacher = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Teacher not found...???"));

        return service.getRecordings(teacher, date , subject , period);
    }

    @GetMapping("/{id}/video")
    public ResponseEntity<Resource> getRecordingVideo(@PathVariable Long id) {

        Resource video = service.getVideoByRecordingId(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(video);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecording(@PathVariable Long id) {

        // 1. Get recording
        ClassRecording recording = classRecordingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recording not found"));

        // 2. Delete video file
        if (recording.getVideoPath() != null) {
            try {
                Path path = Paths.get(recording.getVideoPath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete video file");
            }
        }

        // 3. Delete DB record
        classRecordingRepository.delete(recording);

        return ResponseEntity.ok("Recording deleted successfully");
    }

}
