package com.CollegeHub.DTO.StudentDetailsUpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {

    private Long id;
    private String Role;
    private String name;
    private String email;
    private String rollNumber;
    private String year;
    private String branch;

}
