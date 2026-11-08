package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.service.NotificationAttachmentStorageService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3NotificationAttachmentStorageService
        implements NotificationAttachmentStorageService {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    private static final String FOLDER = "notification-attachments/";

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Attachment cannot be empty"
            );
        }

        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);

        // Generate a unique S3 object key
        String key = FOLDER + UUID.randomUUID() + extension;

        String contentType = file.getContentType();

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        try (InputStream inputStream = file.getInputStream()) {

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            inputStream,
                            file.getSize()
                    )
            );

            return key;

        } catch (IOException | SdkException exception) {

            throw new IllegalStateException(
                    "Failed to upload notification attachment to S3",
                    exception
            );
        }
    }

    @Override
    public void deleteFile(String objectKey) {

        if (objectKey == null || !objectKey.startsWith(FOLDER)) {
            throw new IllegalArgumentException(
                    "Invalid notification attachment reference"
            );
        }

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try {

            s3Client.deleteObject(request);

        } catch (SdkException exception) {

            throw new IllegalStateException(
                    "Failed to delete notification attachment from S3",
                    exception
            );
        }
    }

    private String getFileExtension(String fileName) {

        if (fileName == null) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex < 0) {
            return "";
        }

        String extension = fileName.substring(dotIndex);

        return extension.matches("\\.[a-zA-Z0-9]{1,10}")
                ? extension
                : "";
    }
}
