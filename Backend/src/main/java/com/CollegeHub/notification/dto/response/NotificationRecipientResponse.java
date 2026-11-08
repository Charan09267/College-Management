package com.CollegeHub.notification.dto.response;
import com.CollegeHub.notification.enums.NotificationCategory;
import com.CollegeHub.notification.enums.NotificationPriority;
import com.CollegeHub.notification.enums.RecipientRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRecipientResponse {

    private Long notificationId;

    private String title;

    private String message;

    private NotificationCategory category;

    private String createdByName;

    private RecipientRole createdByRole;

    private NotificationPriority priority;

    private boolean isRead;

    private LocalDateTime publishedAt;

    private List<NotificationAttachmentResponse> attachments;
}