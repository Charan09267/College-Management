package com.CollegeHub.admin.dto.UserCreationDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    private String email;
    private String password;
    private String role;
}
