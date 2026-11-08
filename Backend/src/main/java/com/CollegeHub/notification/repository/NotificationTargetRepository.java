package com.CollegeHub.notification.repository;

import com.CollegeHub.notification.entity.NotificationTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTargetRepository
        extends JpaRepository<NotificationTarget, Long> {

    List<NotificationTarget> findByNotificationId(Long notificationId);

    void deleteByNotificationId(Long notificationId);

}