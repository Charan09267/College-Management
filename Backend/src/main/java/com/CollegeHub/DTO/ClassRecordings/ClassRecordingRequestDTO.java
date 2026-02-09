package com.CollegeHub.DTO.ClassRecordings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRecordingRequestDTO {

    private String title;
    private String description;
    private String subject;

    private LocalDate classDate;
    private Integer periodNumber;
    private String branch;
    private Integer year;

    // getters & setters
}

