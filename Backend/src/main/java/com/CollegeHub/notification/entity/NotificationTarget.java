package com.CollegeHub.notification.entity;

import com.CollegeHub.notification.enums.RecipientRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_targets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_role", nullable = false, length = 20)
    private RecipientRole recipientRole;

    @Column(length = 50)
    private String branch;

    private Integer year;

    @Column(length = 20)
    private String section;

    @Column(length = 50)
    private String batch;
}
