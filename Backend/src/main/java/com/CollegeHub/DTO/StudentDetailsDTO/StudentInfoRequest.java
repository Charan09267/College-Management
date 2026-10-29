package com.CollegeHub.DTO.StudentDetailsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInfoRequest {

        private Long userId;

        private String name;

        private String rollNumber;

        private String profileImageUrl;

        private String branch;

        private String department;

        private Integer year;

        private String section;

        private String batch;
}
