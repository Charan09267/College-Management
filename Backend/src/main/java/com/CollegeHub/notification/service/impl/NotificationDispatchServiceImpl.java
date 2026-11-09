package com.CollegeHub.notification.service.impl;

import com.CollegeHub.admin.entity.ProfileStatus;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationRecipient;
import com.CollegeHub.notification.entity.NotificationTarget;
import com.CollegeHub.notification.enums.NotificationStatus;
import com.CollegeHub.notification.repository.NotificationRecipientRepository;
import com.CollegeHub.notification.repository.NotificationRepository;
import com.CollegeHub.notification.repository.NotificationTargetRepository;
import com.CollegeHub.notification.service.NotificationDispatchService;
import com.CollegeHub.student.entity.StudentInfo;
import com.CollegeHub.student.repository.StudentInfoRepo;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationDispatchServiceImpl
        implements NotificationDispatchService {

    private final NotificationRepository notificationRepository;
    private final NotificationTargetRepository targetRepository;
    private final NotificationRecipientRepository recipientRepository;

    private final StudentInfoRepo studentInfoRepo;
    private final TeacherInfoRepo teacherInfoRepository;

    @Override
    @Transactional
    public void dispatchNotification(Long notificationId) {

        // 1. Retrieve notification
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found: " + notificationId
                ));

        // 2. Idempotency: avoid processing an already published notification
        if (notification.getStatus() == NotificationStatus.PUBLISHED) {
            return;
        }

        if (notification.getStatus() != NotificationStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Notification is not ready for dispatch"
            );
        }

        // 3. Retrieve audience rules
        List<NotificationTarget> targets =
                targetRepository.findByNotificationId(notificationId);

        // 4. Resolve all recipient user IDs
        Set<Long> recipientUserIds = new LinkedHashSet<>();

        for (NotificationTarget target : targets) {
            resolveTarget(target, recipientUserIds);
        }

        // 5. Create recipient records
        List<NotificationRecipient> recipients = recipientUserIds
                .stream()
                .map(userId -> NotificationRecipient.builder()
                        .notification(notification)
                        .userId(userId)
                        .isRead(false)
                        .build())
                .toList();

        // 6. Save recipients in batches
        saveRecipientsInBatches(recipients);

        // 7. Mark notification as published
        notification.setStatus(NotificationStatus.PUBLISHED);
        notification.setPublishedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    private void resolveTarget(
            NotificationTarget target,
            Set<Long> recipientUserIds
    ) {

        switch (target.getRecipientRole()) {

            case STUDENT ->
                    addStudents(target, recipientUserIds);

            case TEACHER ->
                    addTeachers(recipientUserIds);

            case ALL -> {
                addStudents(target, recipientUserIds);
                addTeachers(recipientUserIds);
            }
        }
    }

    private void addStudents(
            NotificationTarget target,
            Set<Long> recipientUserIds
    ) {

        List<StudentInfo> students =
                studentInfoRepo.findMatchingStudents(
                        target.getBranch(),
                        target.getYear(),
                        target.getSection(),
                        target.getBatch()
                );

        students.stream()
                .map(StudentInfo::getUserId)
                .filter(Objects::nonNull)
                .forEach(recipientUserIds::add);
    }

    private void addTeachers(Set<Long> recipientUserIds) {

        List<TeacherInfo> teachers =
                teacherInfoRepository.findByProfileStatus(
                        ProfileStatus.ACTIVE
                );

        teachers.stream()
                .map(TeacherInfo::getUserId)
                .filter(Objects::nonNull)
                .forEach(recipientUserIds::add);
    }

    private void saveRecipientsInBatches(
            List<NotificationRecipient> recipients
    ) {

        int batchSize = 500;

        for (int i = 0; i < recipients.size(); i += batchSize) {

            int end = Math.min(i + batchSize, recipients.size());

            recipientRepository.saveAll(
                    recipients.subList(i, end)
            );
        }
    }
}