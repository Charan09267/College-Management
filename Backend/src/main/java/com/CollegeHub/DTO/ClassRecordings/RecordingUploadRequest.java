package com.CollegeHub.DTO.ClassRecordings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RecordingUploadRequest {

    private String title;

    private String description;

    private String subject;

    private LocalDate classDate;

    private Integer periodNumber;

    private String branch;

    private Integer year;

    private MultipartFile video;
}
