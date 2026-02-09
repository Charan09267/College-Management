package com.CollegeHub.DTO.UserCreationDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    private String email;
    private String password;
    private String role;
}
