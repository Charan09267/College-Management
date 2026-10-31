package com.CollegeHub.DTO.ClassRecordings;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class RecordingResponse {

    private String id;

    private String title;

    private String description;

    private String subject;

    private LocalDate classDate;

    private Integer periodNumber;

    private String branch;

    private Integer year;

    private String originalFileName;

    private Long fileSize;

    private LocalDateTime createdAt;
}
