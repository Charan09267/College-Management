package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationTarget;
import com.CollegeHub.notification.entity.NotificationRecipient;
import com.CollegeHub.notification.enums.NotificationStatus;
import com.CollegeHub.notification.enums.RecipientRole;
import com.CollegeHub.notification.repository.NotificationRepository;
import com.CollegeHub.notification.repository.NotificationTargetRepository;
import com.CollegeHub.notification.repository.NotificationRecipientRepository;
import com.CollegeHub.student.entity.StudentInfo;
import com.CollegeHub.student.repository.StudentInfoRepo;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDispatchServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTargetRepository targetRepository;

    @Mock
    private NotificationRecipientRepository recipientRepository;

    @Mock
    private StudentInfoRepo studentInfoRepo;

    @Mock
    private TeacherInfoRepo teacherInfoRepository;

    @InjectMocks
    private NotificationDispatchServiceImpl dispatchService;

    private static final Long NOTIFICATION_ID = 1L;

    // --------------------------------------------------
    // STUDENT DISPATCH TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldDispatchToMatchingStudents() {

        Notification notification = createProcessingNotification();

        NotificationTarget target = createTarget(
                RecipientRole.STUDENT,
                "CSE",
                3,
                "A",
                null
        );

        List<StudentInfo> students = List.of(
                createStudent(101L),
                createStudent(102L)
        );

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(target));

        when(studentInfoRepo.findMatchingStudents(
                "CSE", 3, "A", null
        )).thenReturn(students);

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(recipientRepository).saveAll(argThat(recipients ->
                countRecipients(recipients) == 2
        ));

        assertEquals(NotificationStatus.PUBLISHED, notification.getStatus());
        assertNotNull(notification.getPublishedAt());

        verify(notificationRepository).save(notification);
    }

    // --------------------------------------------------
    // TEACHER DISPATCH TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldDispatchToActiveTeachers() {

        Notification notification = createProcessingNotification();

        NotificationTarget target = createTarget(
                RecipientRole.TEACHER,
                null, null, null, null
        );

        List<TeacherInfo> teachers = List.of(
                createTeacher(201L),
                createTeacher(202L)
        );

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(target));

        when(teacherInfoRepository.findByProfileStatus(any()))
                .thenReturn(teachers);

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(teacherInfoRepository).findByProfileStatus(any());

        verify(recipientRepository).saveAll(argThat(recipients ->
                countRecipients(recipients) == 2
        ));

        assertEquals(NotificationStatus.PUBLISHED, notification.getStatus());
    }

    // --------------------------------------------------
    // ALL AUDIENCE TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldDispatchToStudentsAndTeachers() {

        Notification notification = createProcessingNotification();

        NotificationTarget target = createTarget(
                RecipientRole.ALL,
                null, null, null, null
        );

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(target));

        when(studentInfoRepo.findMatchingStudents(
                null, null, null, null
        )).thenReturn(List.of(
                createStudent(101L),
                createStudent(102L)
        ));

        when(teacherInfoRepository.findByProfileStatus(any()))
                .thenReturn(List.of(
                        createTeacher(201L),
                        createTeacher(202L)
                ));

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(studentInfoRepo).findMatchingStudents(
                null, null, null, null
        );

        verify(teacherInfoRepository).findByProfileStatus(any());

        verify(recipientRepository).saveAll(argThat(recipients ->
                countRecipients(recipients) == 4
        ));

        assertEquals(NotificationStatus.PUBLISHED, notification.getStatus());
    }

    // --------------------------------------------------
    // DUPLICATE RECIPIENT TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldRemoveDuplicateRecipients() {

        Notification notification = createProcessingNotification();

        NotificationTarget studentTarget = createTarget(
                RecipientRole.STUDENT,
                "CSE", 3, "A", null
        );

        NotificationTarget allTarget = createTarget(
                RecipientRole.ALL,
                null, null, null, null
        );

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(studentTarget, allTarget));

        when(studentInfoRepo.findMatchingStudents(
                any(), any(), any(), any()
        )).thenReturn(List.of(
                createStudent(101L),
                createStudent(101L),
                createStudent(null)
        ));

        when(teacherInfoRepository.findByProfileStatus(any()))
                .thenReturn(List.of(
                        createTeacher(201L),
                        createTeacher(null)
                ));

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(recipientRepository).saveAll(argThat(recipients -> {

            Set<Long> userIds = StreamSupport
                    .stream(recipients.spliterator(), false)
                    .map(NotificationRecipient::getUserId)
                    .collect(Collectors.toSet());

            return userIds.equals(Set.of(101L, 201L))
                    && countRecipients(recipients) == 2;
        }));
    }

    // --------------------------------------------------
    // IDEMPOTENCY TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldSkipAlreadyPublishedNotification() {

        Notification notification = createProcessingNotification();
        notification.setStatus(NotificationStatus.PUBLISHED);

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(targetRepository, never())
                .findByNotificationId(anyLong());

        verifyNoInteractions(recipientRepository);
        verify(notificationRepository, never()).save(any());
    }

    // --------------------------------------------------
    // VALIDATION TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldThrowWhenNotificationDoesNotExist() {

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> dispatchService.dispatchNotification(NOTIFICATION_ID)
        );

        verifyNoInteractions(targetRepository);
        verifyNoInteractions(recipientRepository);
    }

    @Test
    void dispatchNotification_shouldRejectInvalidNotificationStatus() {

        Notification notification = createProcessingNotification();
        notification.setStatus(NotificationStatus.DRAFT);

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        assertThrows(
                IllegalStateException.class,
                () -> dispatchService.dispatchNotification(NOTIFICATION_ID)
        );

        verifyNoInteractions(targetRepository);
        verifyNoInteractions(recipientRepository);
    }

    // --------------------------------------------------
    // BATCH PROCESSING TESTS
    // --------------------------------------------------

    @Test
    void dispatchNotification_shouldSaveRecipientsInBatchesOf500() {

        Notification notification = createProcessingNotification();

        NotificationTarget target = createTarget(
                RecipientRole.STUDENT,
                "CSE", 3, null, null
        );

        List<StudentInfo> students = new ArrayList<>();

        for (long i = 1; i <= 501; i++) {
            students.add(createStudent(i));
        }

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(target));

        when(studentInfoRepo.findMatchingStudents(
                "CSE", 3, null, null
        )).thenReturn(students);

        dispatchService.dispatchNotification(NOTIFICATION_ID);

        verify(recipientRepository, times(2))
                .saveAll(anyList());

        verify(recipientRepository).saveAll(argThat(recipients ->
                countRecipients(recipients) == 500
        ));

        verify(recipientRepository).saveAll(argThat(recipients ->
                countRecipients(recipients) == 1
        ));

        assertEquals(NotificationStatus.PUBLISHED, notification.getStatus());
    }

    // --------------------------------------------------
    // HELPER METHODS
    // --------------------------------------------------

    private Notification createProcessingNotification() {

        return Notification.builder()
                .id(NOTIFICATION_ID)
                .title("Test Announcement")
                .message("Test message")
                .status(NotificationStatus.PROCESSING)
                .build();
    }

    private NotificationTarget createTarget(
            RecipientRole role,
            String branch,
            Integer year,
            String section,
            String batch
    ) {

        return NotificationTarget.builder()
                .recipientRole(role)
                .branch(branch)
                .year(year)
                .section(section)
                .batch(batch)
                .build();
    }

    private StudentInfo createStudent(Long userId) {

        StudentInfo student = new StudentInfo();
        student.setUserId(userId);

        return student;
    }

    private TeacherInfo createTeacher(Long userId) {

        TeacherInfo teacher = new TeacherInfo();
        teacher.setUserId(userId);

        return teacher;
    }

    private long countRecipients(Iterable<NotificationRecipient> recipients) {

        return StreamSupport
                .stream(recipients.spliterator(), false)
                .count();
    }
}
