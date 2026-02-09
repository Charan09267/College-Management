package com.CollegeHub.Services.TeacherService;

import com.CollegeHub.DTO.ClassRecordings.ClassRecordingRequestDTO;
import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.ClassRecordingRepository;
import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClassRecordingService {
    private static final String BASE_DIR = "recordings";

    private final ClassRecordingRepository repository;

    public ClassRecordingService(ClassRecordingRepository repository){
        this.repository = repository;
    }

    public ClassRecording upload(
            MultipartFile video ,
            ClassRecordingRequestDTO request,
            Users uploader
    )throws IOException{

        Path folderPath = Paths.get(
                BASE_DIR,
                request.getYear().toString(),
                request.getBranch().toString(),
                request.getClassDate().toString()
        );

        Files.createDirectories(folderPath);

        //Create Unique file name
        String fileName = "period-" + request.getPeriodNumber()+"_"+System.currentTimeMillis()+"_"+video.getOriginalFilename();

        Path filePath = folderPath.resolve(fileName);


        try(InputStream in = video.getInputStream()){
            Files.copy(in , filePath, StandardCopyOption.REPLACE_EXISTING);
        }


        ClassRecording recording = new ClassRecording();
        recording.setTitle(request.getTitle());
        recording.setDescription(request.getDescription());
        recording.setSubject(request.getSubject().toUpperCase());
        recording.setClassDate(request.getClassDate());
        recording.setPeriodNumber(request.getPeriodNumber());
        recording.setBranch(request.getBranch());
        recording.setYear(request.getYear());
        recording.setVideoPath(filePath.toString());
        recording.setUploadedBy(uploader);

        return repository.save(recording);

    }

    public List<ClassRecording> getRecordings(
            Users teacher ,
            LocalDate date ,
            String subject,
            Integer period
    ){
        if (period != null && date == null && subject == null) {
            throw new IllegalArgumentException("Period alone is not allowed");
        }

        return repository.searchForTeacher(teacher, date, subject, period);
    }

    public Resource getVideoByRecordingId(Long id) {

        ClassRecording recording = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recording not found"));

        Path videoFile = Paths.get(recording.getVideoPath());

        try {
            Resource resource = new UrlResource(videoFile.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Video file not found");
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error loading video", e);
        }
    }


}
