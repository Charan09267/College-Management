package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.request.UpdateNotificationRequest;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.enums.PublisherRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(
            CreateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    );

    NotificationResponse getNotificationById(
            Long notificationId,
            Long publisherId,
            PublisherRole publisherRole
    );

    NotificationResponse updateNotification(
            Long notificationId,
            UpdateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    );

    void deleteNotification(
            Long notificationId,
            Long publisherId,
            PublisherRole publisherRole
    );

}