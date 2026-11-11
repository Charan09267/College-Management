package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.response.NotificationRecipientResponse;
import com.CollegeHub.notification.dto.response.UnreadCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Defines operations for retrieving and managing notifications
 * in the authenticated user's inbox.
 */
public interface UserNotificationService {

    /**
     * Retrieves the user's notifications with pagination.
     *
     * @param userId   ID of the authenticated user
     * @param pageable pagination and sorting information
     * @return a page of notifications with their read status and details
     */
    Page<NotificationRecipientResponse> getMyNotifications(
            Long userId,
            Pageable pageable
    );

    /**
     * Retrieves the total number of unread notifications for a user.
     *
     * @param userId ID of the authenticated user
     * @return unread notification count
     */
    UnreadCountResponse getUnreadCount(Long userId);

    /**
     * Marks a specific notification as read for the given user.
     *
     * @param notificationId ID of the notification
     * @param userId         ID of the user marking it as read
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * Marks all unread notifications as read for the given user.
     *
     * @param userId ID of the authenticated user
     * @return number of notifications updated
     */
    int markAllAsRead(Long userId);
}
