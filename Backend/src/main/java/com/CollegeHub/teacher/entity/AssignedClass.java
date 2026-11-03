package com.CollegeHub.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignedClass {

    private String branch;
    private Integer year;
    private String section;
    private String batch;
}
