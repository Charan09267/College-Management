package com.CollegeHub.notification.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Defines operations for storing and deleting notification attachments
 * in external file storage such as AWS S3.
 */
public interface NotificationAttachmentStorageService {

    /**
     * Uploads a notification attachment to external storage.
     *
     * @param file attachment file to upload
     * @return unique storage object key identifying the uploaded file
     */
    String uploadFile(MultipartFile file);

    /**
     * Deletes an attachment from external storage using its object key.
     *
     * @param objectKey unique storage key of the attachment
     */
    void deleteFile(String objectKey);
}
