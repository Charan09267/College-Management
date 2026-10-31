package com.CollegeHub.Services.StudentService;


import com.CollegeHub.Entity.ClassRecording;
import com.CollegeHub.Entity.StudentInfo;
import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.ClassRecordingRepository;
import com.CollegeHub.Repository.StudentInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class StudentRecordingService {

//    private final ClassRecordingRepository recordingRepo;
//    private final StudentInfoRepo studentInfoRepo;
//
//    public List<ClassRecording> getRecordings(
//            Users user ,
//            LocalDate date,
//            String subject,
//            Integer period
//    ){
//        StudentInfo student = studentInfoRepo.findByUserId(user.getId()).orElseThrow(()->new RuntimeException("Student info not found...???"));
//
//        return recordingRepo.searchForStudent(
//                student.getYear(),
//                student.getBranch(),
//                date,
//                subject,
//                period
//        );
//    }
//
//    public ResponseEntity<Resource> streamVideoById(Long recordingId , Users user , String rangeHeader) throws IOException {
//
//        //1. fetch student info
//        StudentInfo student = studentInfoRepo.findByUserId(user.getId()).orElseThrow(()->new RuntimeException("Student Info not found"));
//
//        //2.fetch recording
//        ClassRecording recording = recordingRepo.findById(recordingId).orElseThrow(() -> new RuntimeException("Recording not found.."));
//
//        // 3. Authorization check
//        if (!recording.getYear().equals(student.getYear()) ||
//                !recording.getBranch().equals(student.getBranch())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//        if (!Boolean.TRUE.equals(recording.getIsActive())) {
//            return ResponseEntity.status(HttpStatus.GONE).build();
//        }
//
//        // 4. Load file
//        Path videoPath = Paths.get(recording.getVideoPath());
//        if (!Files.exists(videoPath)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        long fileSize = Files.size(videoPath);
//
//        long rangeStart = 0;
//        long rangeEnd = fileSize - 1;
//
//        //5. Parse Range header
//        if(rangeHeader != null && rangeHeader.startsWith("bytes=")){
//            String[] ranges = rangeHeader.replace("bytes=","").split("-");
//            rangeStart = Long.parseLong(ranges[0]);
//
//            if(ranges.length > 1 && !ranges[1].isEmpty()){
//                rangeEnd = Long.parseLong(ranges[1]);
//            }
//        }
//
//        if(rangeEnd >= fileSize){
//            rangeEnd = fileSize-1;
//        }
//
//        long contentLength = rangeEnd - rangeStart + 1;
//
//        //6. Create InputStreamResource for partial content
//        InputStream inputStream = Files.newInputStream(videoPath);
//        inputStream.skip(rangeStart);
//
//        Resource resource = new InputStreamResource(inputStream);
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add(HttpHeaders.CONTENT_RANGE ,
//                "bytes "+rangeStart+"-"+rangeEnd+"/"+fileSize);
//        headers.add(HttpHeaders.ACCEPT_RANGES , "bytes");
//        headers.setContentLength(contentLength);
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//
//        return new ResponseEntity<>(resource , headers , HttpStatus.PARTIAL_CONTENT);
//
//    }
}
