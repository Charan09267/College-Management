package com.CollegeHub.DTO.UserCreationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateResponse {
    private String email;
    private Long id;
    private String message;
}
