package com.CollegeHub.notification.controller;

import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.exception.ResourceNotFoundException;
import com.CollegeHub.notification.dto.request.UpdateNotificationRequest;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.enums.PublisherRole;
import com.CollegeHub.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/manage")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
public class NotificationManagementController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {

        Users user = getAuthenticatedUser(authentication);

        NotificationResponse response =
                notificationService.getNotificationById(
                        notificationId,
                        user.getId(),
                        getPublisherRole(authentication)
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            value = "/{notificationId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<NotificationResponse> updateNotification(
            @PathVariable Long notificationId,
            @RequestPart("request") @Valid UpdateNotificationRequest request,
            @RequestPart(value = "attachments", required = false)
            List<MultipartFile> attachments,
            Authentication authentication
    ) {

        Users user = getAuthenticatedUser(authentication);

        NotificationResponse response =
                notificationService.updateNotification(
                        notificationId,
                        request,
                        attachments,
                        user.getId(),
                        getPublisherRole(authentication)
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {

        Users user = getAuthenticatedUser(authentication);

        notificationService.deleteNotification(
                notificationId,
                user.getId(),
                getPublisherRole(authentication)
        );

        return ResponseEntity.noContent().build();
    }

    private Users getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private PublisherRole getPublisherRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"))
                ? PublisherRole.ADMIN
                : PublisherRole.TEACHER;
    }
}