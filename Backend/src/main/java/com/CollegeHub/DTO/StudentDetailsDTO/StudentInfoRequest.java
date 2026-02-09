package com.CollegeHub.DTO.StudentDetailsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StudentInfoRequest {
        private Long userId;
        private String name;
        private String rollNumber;
        private String branch;
        private Integer year;
}
