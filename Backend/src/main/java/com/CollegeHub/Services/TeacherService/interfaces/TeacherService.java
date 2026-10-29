package com.CollegeHub.Services.TeacherService.interfaces;

import com.CollegeHub.DTO.TeacherDetailsDTO.TeacherDetailsResponse;
import org.springframework.http.ResponseEntity;

public interface TeacherService {
    public ResponseEntity<TeacherDetailsResponse> getDetails(String email);
}
