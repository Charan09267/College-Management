package com.CollegeHub.notification.repository;

import com.CollegeHub.notification.entity.NotificationRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NotificationRecipientRepository
        extends JpaRepository<NotificationRecipient, Long> {

    Page<NotificationRecipient> findByUserIdOrderByDeliveredAtDesc(
            Long userId,
            Pageable pageable
    );

    Optional<NotificationRecipient> findByNotificationIdAndUserId(
            Long notificationId,
            Long userId
    );

    long countByUserIdAndIsReadFalse(Long userId);

    boolean existsByNotificationIdAndUserId(
            Long notificationId,
            Long userId
    );

    @Modifying
    @Query("""
        UPDATE NotificationRecipient nr
        SET nr.isRead = true,
            nr.readAt = :readAt
        WHERE nr.notification.id = :notificationId
          AND nr.userId = :userId
          AND nr.isRead = false
    """)
    int markAsRead(
            @Param("notificationId") Long notificationId,
            @Param("userId") Long userId,
            @Param("readAt") LocalDateTime readAt
    );

    @Modifying
    @Query("""
        UPDATE NotificationRecipient nr
        SET nr.isRead = true,
            nr.readAt = :readAt
        WHERE nr.userId = :userId
          AND nr.isRead = false
    """)
    int markAllAsRead(
            @Param("userId") Long userId,
            @Param("readAt") LocalDateTime readAt
    );
}
