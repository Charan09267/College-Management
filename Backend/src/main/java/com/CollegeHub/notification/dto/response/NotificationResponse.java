package com.CollegeHub.notification.dto.response;


import com.CollegeHub.notification.enums.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private NotificationCategory category;

    private Long createdByUserId;

    private PublisherRole createdByRole;

    private String createdByName;

    private NotificationPriority priority;

    private NotificationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    private LocalDateTime expiresAt;

    private List<NotificationTargetResponse> targets;

    private List<NotificationAttachmentResponse> attachments;
}