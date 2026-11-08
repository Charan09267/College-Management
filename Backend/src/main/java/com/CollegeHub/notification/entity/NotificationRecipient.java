package com.CollegeHub.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notification_recipients",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_recipient",
                        columnNames = {"notification_id", "user_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_recipient_user_read",
                        columnList = "user_id, is_read"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "delivered_at", nullable = false, updatable = false)
    private LocalDateTime deliveredAt;

    @PrePersist
    protected void onCreate() {
        deliveredAt = LocalDateTime.now();

        if (isRead == null) {
            isRead = false;
        }
    }
}
