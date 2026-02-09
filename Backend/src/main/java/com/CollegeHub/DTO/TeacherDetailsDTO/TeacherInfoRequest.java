package com.CollegeHub.DTO.TeacherDetailsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherInfoRequest {
    private Long userId;
    private String teacherId;
    private String department;
    private String name;
}
