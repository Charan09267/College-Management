package com.CollegeHub.notification.service;

import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.request.NotificationTargetRequest;
import com.CollegeHub.notification.dto.response.NotificationResponse;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationOutbox;
import com.CollegeHub.notification.entity.NotificationTarget;
import com.CollegeHub.notification.enums.*;
import com.CollegeHub.notification.repository.NotificationOutboxRepository;
import com.CollegeHub.notification.repository.NotificationRepository;
import com.CollegeHub.notification.repository.NotificationTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTargetRepository targetRepository;
    private final NotificationOutboxRepository outboxRepository;

    @Override
    @Transactional
    public NotificationResponse createNotification(
            CreateNotificationRequest request,
            List<MultipartFile> attachments,
            Long publisherId,
            PublisherRole publisherRole
    ) {

        // 1. Validate audience
        validateTargets(request.getTargets(), publisherRole);

        // 2. Create notification
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

        // 3. Save audience targets
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

        // 4. Create outbox event
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
                .attachments(List.of())
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
}
