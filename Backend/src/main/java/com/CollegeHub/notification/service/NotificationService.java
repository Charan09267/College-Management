package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
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

}