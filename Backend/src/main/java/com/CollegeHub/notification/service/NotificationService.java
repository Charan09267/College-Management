package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.request.UpdateNotificationRequest;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.enums.PublisherRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationService {

    /**
     * Creates a new notification and initiates its asynchronous distribution.
     *
     * Validates the notification targets and attachments, saves the notification
     * and target criteria, uploads attachments to S3, and creates an outbox event
     * for subsequent recipient distribution through RabbitMQ.
     *
     * @param request       notification details, including title, message,
     *                      category, priority, expiry and target criteria
     * @param attachments  optional files attached to the notification
     * @param publisherId   ID of the authenticated notification creator
     * @param publisherRole role of the creator (TEACHER or ADMIN)
     * @return              the created notification with its targets and attachments
     */
    NotificationResponse createNotification(
            CreateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    );

    /**
     * Retrieves the complete details of a published notification owned
     * by the specified publisher.
     *
     * Validates ownership and notification status, then maps the notification
     * entity, publisher details, target criteria and attachment metadata
     * into a response DTO.
     *
     * @param notificationId ID of the notification to retrieve
     * @param publisherId    ID of the authenticated publisher
     * @param publisherRole role of the authenticated publisher
     * @return               complete notification response
     */
    NotificationResponse getNotificationById(
            Long notificationId,
            Long publisherId,
            PublisherRole publisherRole
    );

    /**
     * Updates an existing published notification owned by the specified publisher.
     *
     * Allows modification of the notification title and message. If a new
     * attachment list is provided, replaces the existing attachments and
     * schedules the old S3 objects for deletion after transaction commit.
     *
     * The original audience criteria, category, priority and publisher
     * information remain unchanged.
     *
     * @param notificationId ID of the notification to update
     * @param request        updated notification title and message
     * @param attachments    optional replacement attachment list; null retains
     *                       existing attachments, while an empty list removes them
     * @param publisherId    ID of the authenticated publisher
     * @param publisherRole role of the authenticated publisher
     * @return               updated notification response
     */
    NotificationResponse updateNotification(
            Long notificationId,
            UpdateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    );

    /**
     * Deletes a published notification owned by the specified publisher.
     *
     * Removes the notification's attachment metadata and notification record
     * from the database, then schedules the associated S3 objects for deletion
     * after the database transaction commits.
     *
     * @param notificationId ID of the notification to delete
     * @param publisherId    ID of the authenticated publisher
     * @param publisherRole role of the authenticated publisher
     */
    void deleteNotification(
            Long notificationId,
            Long publisherId,
            PublisherRole publisherRole
    );

}