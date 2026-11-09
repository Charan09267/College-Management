package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.dto.response.NotificationAttachmentResponse;
import com.CollegeHub.notification.dto.response.NotificationRecipientResponse;
import com.CollegeHub.notification.dto.response.UnreadCountResponse;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationRecipient;
import com.CollegeHub.notification.enums.PublisherRole;
import com.CollegeHub.notification.repository.NotificationAttachmentRepository;
import com.CollegeHub.notification.repository.NotificationRecipientRepository;
import com.CollegeHub.notification.service.UserNotificationService;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl
        implements UserNotificationService {

    private final NotificationRecipientRepository recipientRepository;
    private final NotificationAttachmentRepository attachmentRepository;
    private final TeacherInfoRepo teacherInfoRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationRecipientResponse> getMyNotifications(
            Long userId,
            Pageable pageable
    ) {

        Page<NotificationRecipient> recipients =
                recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                        userId,
                        pageable
                );

        return recipients.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UnreadCountResponse getUnreadCount(Long userId) {

        long unreadCount =
                recipientRepository.countByUserIdAndIsReadFalse(userId);

        return UnreadCountResponse.builder()
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {

        NotificationRecipient recipient = recipientRepository
                .findByNotificationIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found in your inbox"
                ));

        // Avoid updating an already-read notification
        if (Boolean.TRUE.equals(recipient.getIsRead())) {
            return;
        }

        recipientRepository.markAsRead(
                notificationId,
                userId,
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public int markAllAsRead(Long userId) {

        return recipientRepository.markAllAsRead(
                userId,
                LocalDateTime.now()
        );
    }

    private NotificationRecipientResponse mapToResponse(
            NotificationRecipient recipient
    ) {

        Notification notification = recipient.getNotification();

        return NotificationRecipientResponse.builder()
                .notificationId(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .category(notification.getCategory())
                .createdByName(getPublisherName(notification))
                .createdByRole(notification.getCreatedByRole())
                .priority(notification.getPriority())
                .isRead(Boolean.TRUE.equals(recipient.getIsRead()))
                .publishedAt(notification.getPublishedAt())
                .attachments(mapAttachments(notification.getId()))
                .build();
    }

    private String getPublisherName(Notification notification) {

        if (notification.getCreatedByRole() == PublisherRole.ADMIN) {
            return "Admin";
        }

        TeacherInfo teacher = teacherInfoRepository
                .findByUserId(notification.getCreatedByUserId())
                .orElseThrow(() -> new IllegalStateException(
                        "Teacher profile not found"
                ));

        return Stream.of(
                        teacher.getFirstName(),
                        teacher.getMiddleName(),
                        teacher.getLastName()
                )
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.joining(" "));
    }

    private List<NotificationAttachmentResponse> mapAttachments(
            Long notificationId
    ) {

        return attachmentRepository.findByNotificationId(notificationId)
                .stream()
                .map(attachment ->
                        NotificationAttachmentResponse.builder()
                                .id(attachment.getId())
                                .fileName(attachment.getFileName())
                                .contentType(attachment.getContentType())
                                .fileSize(attachment.getFileSize())
                                .createdAt(attachment.getCreatedAt())
                                .build()
                )
                .toList();
    }
}
