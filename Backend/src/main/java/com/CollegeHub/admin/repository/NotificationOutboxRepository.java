package com.CollegeHub.admin.repository;

import com.CollegeHub.notification.entity.NotificationOutbox;
import com.CollegeHub.notification.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationOutboxRepository
        extends JpaRepository<NotificationOutbox, Long> {

    List<NotificationOutbox> findTop100ByStatusOrderByCreatedAtAsc(
            OutboxStatus status
    );

    List<NotificationOutbox> findByNotificationId(Long notificationId);

}
