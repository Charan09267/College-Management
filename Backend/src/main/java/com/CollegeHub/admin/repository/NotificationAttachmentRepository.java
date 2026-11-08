package com.CollegeHub.admin.repository;

import com.CollegeHub.notification.entity.NotificationAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationAttachmentRepository
        extends JpaRepository<NotificationAttachment, Long> {

    List<NotificationAttachment> findByNotificationId(Long notificationId);

    Optional<NotificationAttachment> findByIdAndNotificationId(
            Long id,
            Long notificationId
    );

    long countByNotificationId(Long notificationId);
}
