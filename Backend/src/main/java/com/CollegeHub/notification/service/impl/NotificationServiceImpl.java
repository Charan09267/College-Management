package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.request.NotificationTargetRequest;
import com.CollegeHub.notification.dto.response.NotificationAttachmentResponse;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationAttachment;
import com.CollegeHub.notification.entity.NotificationOutbox;
import com.CollegeHub.notification.entity.NotificationTarget;
import com.CollegeHub.notification.enums.*;
import com.CollegeHub.notification.repository.NotificationAttachmentRepository;
import com.CollegeHub.notification.repository.NotificationOutboxRepository;
import com.CollegeHub.notification.repository.NotificationRepository;
import com.CollegeHub.notification.repository.NotificationTargetRepository;
import com.CollegeHub.notification.service.NotificationAttachmentStorageService;
import com.CollegeHub.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTargetRepository targetRepository;
    private final NotificationOutboxRepository outboxRepository;
    private final NotificationAttachmentRepository attachmentRepository;
    private final NotificationAttachmentStorageService attachmentStorageService;

    private static final int MAX_ATTACHMENTS = 5;

    private static final long MAX_FILE_SIZE =
            10 * 1024 * 1024L;

    private static final Map<String, String> ALLOWED_FILES = Map.of(
            "pdf", "application/pdf",
            "doc", "application/msword",
            "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "ppt", "application/vnd.ms-powerpoint",
            "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png"
    );

    @Override
    @Transactional
    public NotificationResponse createNotification(
            CreateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    ) {

        validateAttachments(attachments);

        validateTargets(request.getTargets(), publisherRole);

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .category(request.getCategory())
                .createdByUserId(publisherId)
                .createdByRole(publisherRole)
                .priority(request.getPriority() != null
                        ? request.getPriority()
                        : NotificationPriority.NORMAL)
                .status(NotificationStatus.PROCESSING)
                .data(request.getData())
                .expiresAt(request.getExpiresAt())
                .build();

        Notification savedNotification =
                notificationRepository.save(notification);

        List<NotificationTarget> targets = request.getTargets()
                .stream()
                .map(target -> NotificationTarget.builder()
                        .notification(savedNotification)
                        .recipientRole(target.getRecipientRole())
                        .branch(target.getBranch())
                        .year(target.getYear())
                        .section(target.getSection())
                        .batch(target.getBatch())
                        .build())
                .toList();

        targetRepository.saveAll(targets);

        List<NotificationAttachmentResponse> attachmentResponses =
                saveAttachments(savedNotification, attachments);

        NotificationOutbox outbox = NotificationOutbox.builder()
                .notification(savedNotification)
                .eventType("NOTIFICATION_CREATED")
                .payload(Map.of(
                        "notificationId", savedNotification.getId()
                ))
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .build();

        outboxRepository.save(outbox);

        // 5. Return response
        return NotificationResponse.builder()
                .id(savedNotification.getId())
                .title(savedNotification.getTitle())
                .message(savedNotification.getMessage())
                .category(savedNotification.getCategory())
                .createdByUserId(savedNotification.getCreatedByUserId())
                .createdByRole(savedNotification.getCreatedByRole())
                .priority(savedNotification.getPriority())
                .status(savedNotification.getStatus())
                .createdAt(savedNotification.getCreatedAt())
                .updatedAt(savedNotification.getUpdatedAt())
                .expiresAt(savedNotification.getExpiresAt())
                .attachments(attachmentResponses)
                .build();
    }

    private void validateTargets(
            List<NotificationTargetRequest> targets,
            PublisherRole publisherRole
    ) {
        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one target is required"
            );
        }

        for (NotificationTargetRequest target : targets) {

            RecipientRole recipientRole = target.getRecipientRole();

            // Teachers can notify only students
            if (publisherRole == PublisherRole.TEACHER
                    && recipientRole != RecipientRole.STUDENT) {
                throw new IllegalArgumentException(
                        "Teachers can send notifications only to students"
                );
            }

            // ALL cannot have additional filters
            if (recipientRole == RecipientRole.ALL) {

                if (target.getBranch() != null
                        || target.getYear() != null
                        || target.getSection() != null
                        || target.getBatch() != null) {
                    throw new IllegalArgumentException(
                            "ALL audience cannot have additional filters"
                    );
                }
            }

            // Validate student year
            if (target.getYear() != null
                    && (target.getYear() < 1 || target.getYear() > 4)) {
                throw new IllegalArgumentException(
                        "Year must be between 1 and 4"
                );
            }
        }
    }

    private void validateAttachments(List<MultipartFile> attachments) {

        if (attachments == null || attachments.isEmpty()) {
            return;
        }

        if (attachments.size() > MAX_ATTACHMENTS) {
            throw new IllegalArgumentException(
                    "Maximum 5 attachments are allowed"
            );
        }

        for (MultipartFile file : attachments) {

            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(
                        "Attachment cannot be empty"
                );
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException(
                        "Each attachment must be smaller than or equal to 10 MB"
                );
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null || !fileName.contains(".")) {
                throw new IllegalArgumentException(
                        "Invalid attachment filename"
                );
            }

            String extension = fileName.substring(
                    fileName.lastIndexOf(".") + 1
            ).toLowerCase(Locale.ROOT);

            String expectedContentType = ALLOWED_FILES.get(extension);

            if (expectedContentType == null) {
                throw new IllegalArgumentException(
                        "Unsupported attachment format: " + extension
                );
            }

            if (!expectedContentType.equalsIgnoreCase(file.getContentType())) {
                throw new IllegalArgumentException(
                        "Invalid content type for file: " + fileName
                );
            }
        }
    }

    private void registerS3RollbackCleanup(List<String> uploadedKeys) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCompletion(int status) {

                        if (status == STATUS_ROLLED_BACK) {

                            for (String key : uploadedKeys) {
                                try {
                                    attachmentStorageService.deleteFile(key);
                                } catch (Exception exception) {
                                    log.error(
                                            "Failed to clean up S3 attachment: {}",
                                            key,
                                            exception
                                    );
                                }
                            }
                        }
                    }
                }
        );
    }

    private List<NotificationAttachmentResponse> saveAttachments(
            Notification notification,
            List<MultipartFile> attachments
    ) {

        if (attachments == null || attachments.isEmpty()) {
            return List.of();
        }

        List<String> uploadedKeys = new ArrayList<>();

        // Register cleanup before uploading anything
        registerS3RollbackCleanup(uploadedKeys);

        List<NotificationAttachment> savedAttachments = new ArrayList<>();

        for (MultipartFile file : attachments) {

            String objectKey = attachmentStorageService.uploadFile(file);

            uploadedKeys.add(objectKey);

            NotificationAttachment attachment =
                    NotificationAttachment.builder()
                            .notification(notification)
                            .fileName(file.getOriginalFilename())
                            .s3ObjectKey(objectKey)
                            .contentType(file.getContentType())
                            .fileSize(file.getSize())
                            .build();

            savedAttachments.add(attachment);
        }

        List<NotificationAttachment> persistedAttachments =
                attachmentRepository.saveAll(savedAttachments);

        return persistedAttachments.stream()
                .map(attachment -> NotificationAttachmentResponse.builder()
                        .id(attachment.getId())
                        .fileName(attachment.getFileName())
                        .contentType(attachment.getContentType())
                        .fileSize(attachment.getFileSize())
                        .createdAt(attachment.getCreatedAt())
                        .build())
                .toList();
    }
}
