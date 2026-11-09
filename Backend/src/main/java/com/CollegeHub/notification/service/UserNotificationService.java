package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.response.NotificationRecipientResponse;
import com.CollegeHub.notification.dto.response.UnreadCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserNotificationService {

    Page<NotificationRecipientResponse> getMyNotifications(
            Long userId,
            Pageable pageable
    );

    UnreadCountResponse getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    int markAllAsRead(Long userId);
}
