package com.CollegeHub.Entity;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "class_recordings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRecording {

    @Id
    private String id;

    private String title;

    private String description;

    private String subject;

    private LocalDate classDate;

    private Integer periodNumber;

    private String branch;

    private Integer year;

    // MySQL Users.id of the teacher who uploaded this recording
    @Indexed
    private Long uploadedByUserId;

    // Internal local storage path; never expose this in API responses
    private String videoPath;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime createdAt;
}