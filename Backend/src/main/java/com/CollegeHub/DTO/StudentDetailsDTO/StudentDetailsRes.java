package com.CollegeHub.DTO.StudentDetailsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetailsRes {
    private String email;
    private String role;
    private String name;
    private String rollNumber;
    private String year;
    private String branch;
    private Long userId;
}
