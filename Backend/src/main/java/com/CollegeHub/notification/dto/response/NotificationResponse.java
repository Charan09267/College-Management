package com.CollegeHub.notification.dto.response;


import com.CollegeHub.notification.enums.NotificationCategory;
import com.CollegeHub.notification.enums.NotificationPriority;
import com.CollegeHub.notification.enums.NotificationStatus;
import com.CollegeHub.notification.enums.RecipientRole;
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

    private RecipientRole createdByRole;

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