package com.CollegeHub.DTO.UserInformationDetails;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetailsResponse {

    private Long id;
    private String name;
    private String email;
    private String teacher_id;
    private String department;

}
