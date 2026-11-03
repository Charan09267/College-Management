package com.CollegeHub.recording.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordingDetailsResponse {

    private String id;

    private String title;
    private String description;
    private String subject;

    private LocalDate classDate;
    private Integer periodNumber;

    private String branch;
    private Integer year;

    private Long uploadedByUserId;

    private String originalFileName;
    private String contentType;
    private Long fileSize;

    private LocalDateTime createdAt;
}
