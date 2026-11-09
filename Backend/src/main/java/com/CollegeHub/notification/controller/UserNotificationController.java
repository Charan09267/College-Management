package com.CollegeHub.notification.controller;

import com.CollegeHub.admin.repository.UserRepository;
import com.CollegeHub.exception.ResourceNotFoundException;
import com.CollegeHub.notification.dto.response.NotificationRecipientResponse;
import com.CollegeHub.notification.dto.response.UnreadCountResponse;
import com.CollegeHub.notification.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<NotificationRecipientResponse>> getMyNotifications(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "deliveredAt",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                userNotificationService.getMyNotifications(userId, pageable)
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(
            Authentication authentication
    ) {

        Long userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                userNotificationService.getUnreadCount(userId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {

        Long userId = getAuthenticatedUserId(authentication);

        userNotificationService.markAsRead(notificationId, userId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, Integer>> markAllAsRead(
            Authentication authentication
    ) {

        Long userId = getAuthenticatedUserId(authentication);

        int updatedCount = userNotificationService.markAllAsRead(userId);

        return ResponseEntity.ok(
                Map.of("updatedCount", updatedCount)
        );
    }

    private Long getAuthenticatedUserId(Authentication authentication) {

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                )
                .getId();
    }
}
