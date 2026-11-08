package com.CollegeHub.notification.service;

import org.springframework.web.multipart.MultipartFile;

public interface NotificationAttachmentStorageService {

    String uploadFile(MultipartFile file);

    void deleteFile(String objectKey);
}
