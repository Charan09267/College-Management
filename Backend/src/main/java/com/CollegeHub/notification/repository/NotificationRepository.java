package com.CollegeHub.notification.repository;

import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.enums.NotificationStatus;
import com.CollegeHub.notification.enums.PublisherRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndStatus(
            Long id,
            NotificationStatus status
    );

    List<Notification> findByCreatedByUserIdOrderByCreatedAtDesc(
            Long createdByUserId
    );

    Page<Notification> findByCreatedByUserIdAndCreatedByRoleOrderByCreatedAtDesc(
            Long createdByUserId,
            PublisherRole createdByRole,
            Pageable pageable
    );

}