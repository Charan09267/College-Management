package com.CollegeHub.admin.dto.UserCreationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateResponse {
    private String email;
    private Long id;
    private String message;
}
