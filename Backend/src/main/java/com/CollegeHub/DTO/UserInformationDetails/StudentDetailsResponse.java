package com.CollegeHub.DTO.UserInformationDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class StudentDetailsResponse {

    private Long id;
    private String name;
    private String email;
    private String rollNumber;
    private String year;
    private String branch;

}
