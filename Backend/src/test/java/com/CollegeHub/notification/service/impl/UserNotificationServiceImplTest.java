package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.dto.response.*;
import com.CollegeHub.notification.entity.*;
import com.CollegeHub.notification.enums.*;
import com.CollegeHub.notification.repository.*;
import com.CollegeHub.teacher.entity.TeacherInfo;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceImplTest {

    @Mock
    private NotificationRecipientRepository recipientRepository;

    @Mock
    private NotificationAttachmentRepository attachmentRepository;

    @Mock
    private TeacherInfoRepo teacherInfoRepository;

    @InjectMocks
    private UserNotificationServiceImpl userNotificationService;

    private static final Long USER_ID = 10L;
    private static final Long NOTIFICATION_ID = 1L;

    // --------------------------------------------------
    // GET MY NOTIFICATIONS TESTS
    // --------------------------------------------------

    @Test
    void getMyNotifications_shouldReturnPaginatedNotifications() {

        Pageable pageable = PageRequest.of(0, 10);

        Notification notification = createNotification(
                PublisherRole.ADMIN
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                false
        );

        when(recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                USER_ID,
                pageable
        )).thenReturn(new PageImpl<>(List.of(recipient), pageable, 1));

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        Page<NotificationRecipientResponse> response =
                userNotificationService.getMyNotifications(
                        USER_ID,
                        pageable
                );

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());

        NotificationRecipientResponse result =
                response.getContent().get(0);

        assertEquals(NOTIFICATION_ID, result.getNotificationId());
        assertEquals("Test Announcement", result.getTitle());
        assertEquals("Test message", result.getMessage());
        assertEquals("Admin", result.getCreatedByName());
        assertFalse(result.isRead());

        verify(recipientRepository)
                .findByUserIdOrderByDeliveredAtDesc(USER_ID, pageable);
    }

    @Test
    void getMyNotifications_shouldReturnEmptyPageWhenNoNotificationsExist() {

        Pageable pageable = PageRequest.of(0, 10);

        when(recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                USER_ID,
                pageable
        )).thenReturn(Page.empty(pageable));

        Page<NotificationRecipientResponse> response =
                userNotificationService.getMyNotifications(
                        USER_ID,
                        pageable
                );

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verifyNoInteractions(attachmentRepository);
        verifyNoInteractions(teacherInfoRepository);
    }

    @Test
    void getMyNotifications_shouldMapTeacherPublisherName() {

        Notification notification = createNotification(
                PublisherRole.TEACHER
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                false
        );

        TeacherInfo teacher = new TeacherInfo();
        teacher.setFirstName("Arun");
        teacher.setMiddleName("Kumar");
        teacher.setLastName("Reddy");

        Pageable pageable = PageRequest.of(0, 10);

        when(recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                USER_ID,
                pageable
        )).thenReturn(new PageImpl<>(List.of(recipient)));

        when(teacherInfoRepository.findByUserId(
                notification.getCreatedByUserId()
        )).thenReturn(Optional.of(teacher));

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        Page<NotificationRecipientResponse> response =
                userNotificationService.getMyNotifications(
                        USER_ID,
                        pageable
                );

        assertEquals(
                "Arun Kumar Reddy",
                response.getContent().get(0).getCreatedByName()
        );
    }

    @Test
    void getMyNotifications_shouldMapAttachmentDetails() {

        Notification notification = createNotification(
                PublisherRole.ADMIN
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                false
        );

        NotificationAttachment attachment =
                NotificationAttachment.builder()
                        .id(5L)
                        .fileName("announcement.pdf")
                        .contentType("application/pdf")
                        .fileSize(1024L)
                        .build();

        Pageable pageable = PageRequest.of(0, 10);

        when(recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                USER_ID,
                pageable
        )).thenReturn(new PageImpl<>(List.of(recipient)));

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of(attachment));

        Page<NotificationRecipientResponse> response =
                userNotificationService.getMyNotifications(
                        USER_ID,
                        pageable
                );

        List<NotificationAttachmentResponse> attachments =
                response.getContent().get(0).getAttachments();

        assertEquals(1, attachments.size());
        assertEquals(5L, attachments.get(0).getId());
        assertEquals("announcement.pdf", attachments.get(0).getFileName());
        assertEquals("application/pdf", attachments.get(0).getContentType());
        assertEquals(1024L, attachments.get(0).getFileSize());
    }

    @Test
    void getMyNotifications_shouldThrowWhenTeacherProfileDoesNotExist() {

        Notification notification = createNotification(
                PublisherRole.TEACHER
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                false
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(recipientRepository.findByUserIdOrderByDeliveredAtDesc(
                USER_ID,
                pageable
        )).thenReturn(new PageImpl<>(List.of(recipient)));

        when(teacherInfoRepository.findByUserId(
                notification.getCreatedByUserId()
        )).thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> userNotificationService.getMyNotifications(
                        USER_ID,
                        pageable
                )
        );
    }

    // --------------------------------------------------
    // UNREAD COUNT TESTS
    // --------------------------------------------------

    @Test
    void getUnreadCount_shouldReturnCorrectUnreadCount() {

        when(recipientRepository.countByUserIdAndIsReadFalse(USER_ID))
                .thenReturn(5L);

        UnreadCountResponse response =
                userNotificationService.getUnreadCount(USER_ID);

        assertNotNull(response);
        assertEquals(5L, response.getUnreadCount());

        verify(recipientRepository)
                .countByUserIdAndIsReadFalse(USER_ID);
    }

    @Test
    void getUnreadCount_shouldReturnZeroWhenNoUnreadNotificationsExist() {

        when(recipientRepository.countByUserIdAndIsReadFalse(USER_ID))
                .thenReturn(0L);

        UnreadCountResponse response =
                userNotificationService.getUnreadCount(USER_ID);

        assertEquals(0L, response.getUnreadCount());
    }

    // --------------------------------------------------
    // MARK AS READ TESTS
    // --------------------------------------------------

    @Test
    void markAsRead_shouldMarkUnreadNotificationAsRead() {

        Notification notification = createNotification(
                PublisherRole.ADMIN
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                false
        );

        when(recipientRepository.findByNotificationIdAndUserId(
                NOTIFICATION_ID,
                USER_ID
        )).thenReturn(Optional.of(recipient));

        userNotificationService.markAsRead(
                NOTIFICATION_ID,
                USER_ID
        );

        verify(recipientRepository).markAsRead(
                eq(NOTIFICATION_ID),
                eq(USER_ID),
                any()
        );
    }

    @Test
    void markAsRead_shouldNotUpdateAlreadyReadNotification() {

        Notification notification = createNotification(
                PublisherRole.ADMIN
        );

        NotificationRecipient recipient = createRecipient(
                notification,
                true
        );

        when(recipientRepository.findByNotificationIdAndUserId(
                NOTIFICATION_ID,
                USER_ID
        )).thenReturn(Optional.of(recipient));

        userNotificationService.markAsRead(
                NOTIFICATION_ID,
                USER_ID
        );

        verify(recipientRepository, never()).markAsRead(
                anyLong(),
                anyLong(),
                any()
        );
    }

    @Test
    void markAsRead_shouldThrowWhenNotificationIsNotInUserInbox() {

        when(recipientRepository.findByNotificationIdAndUserId(
                NOTIFICATION_ID,
                USER_ID
        )).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> userNotificationService.markAsRead(
                        NOTIFICATION_ID,
                        USER_ID
                )
        );

        verify(recipientRepository, never()).markAsRead(
                anyLong(),
                anyLong(),
                any()
        );
    }

    // --------------------------------------------------
    // MARK ALL AS READ TESTS
    // --------------------------------------------------

    @Test
    void markAllAsRead_shouldUpdateAllUnreadNotifications() {

        when(recipientRepository.markAllAsRead(
                eq(USER_ID),
                any()
        )).thenReturn(5);

        int updatedCount =
                userNotificationService.markAllAsRead(USER_ID);

        assertEquals(5, updatedCount);

        verify(recipientRepository).markAllAsRead(
                eq(USER_ID),
                any()
        );
    }

    @Test
    void markAllAsRead_shouldReturnZeroWhenNothingIsUpdated() {

        when(recipientRepository.markAllAsRead(
                eq(USER_ID),
                any()
        )).thenReturn(0);

        int updatedCount =
                userNotificationService.markAllAsRead(USER_ID);

        assertEquals(0, updatedCount);
    }

    // --------------------------------------------------
    // HELPER METHODS
    // --------------------------------------------------

    private Notification createNotification(PublisherRole publisherRole) {

        return Notification.builder()
                .id(NOTIFICATION_ID)
                .title("Test Announcement")
                .message("Test message")
                .category(NotificationCategory.GENERAL)
                .createdByUserId(20L)
                .createdByRole(publisherRole)
                .priority(NotificationPriority.NORMAL)
                .status(NotificationStatus.PUBLISHED)
                .build();
    }

    private NotificationRecipient createRecipient(
            Notification notification,
            boolean isRead
    ) {

        return NotificationRecipient.builder()
                .notification(notification)
                .userId(USER_ID)
                .isRead(isRead)
                .build();
    }
}
