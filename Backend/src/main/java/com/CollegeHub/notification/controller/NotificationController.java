package com.CollegeHub.notification.controller;

import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.exception.ResourceNotFoundException;
import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.enums.PublisherRole;
import com.CollegeHub.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping(
            value = "/teacher/notifications",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<NotificationResponse> createTeacherNotification(
            @RequestPart("request") @Valid CreateNotificationRequest request,
            @RequestPart(value = "attachments", required = false)
            List<MultipartFile> attachments,
            Authentication authentication
    ) {

        Users user = getAuthenticatedUser(authentication);

        NotificationResponse response =
                notificationService.createNotification(
                        request,
                        attachments,
                        user.getId(),
                        PublisherRole.TEACHER
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            value = "/admin/notifications",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> createAdminNotification(
            @RequestPart("request") @Valid CreateNotificationRequest request,
            @RequestPart(value = "attachments", required = false)
            List<MultipartFile> attachments,
            Authentication authentication
    ) {

        Users user = getAuthenticatedUser(authentication);

        NotificationResponse response =
                notificationService.createNotification(
                        request,
                        attachments,
                        user.getId(),
                        PublisherRole.ADMIN
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Users getAuthenticatedUser(Authentication authentication) {

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }
}
