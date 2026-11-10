package com.CollegeHub.notification.service.impl;

import com.CollegeHub.exception.ResourceNotFoundException;
import com.CollegeHub.notification.dto.request.CreateNotificationRequest;
import com.CollegeHub.notification.dto.request.NotificationTargetRequest;
import com.CollegeHub.notification.dto.request.UpdateNotificationRequest;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationAttachment;
import com.CollegeHub.notification.enums.*;
import com.CollegeHub.notification.repository.*;
import com.CollegeHub.notification.service.NotificationAttachmentStorageService;
import com.CollegeHub.teacher.repository.TeacherInfoRepo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTargetRepository targetRepository;

    @Mock
    private NotificationOutboxRepository outboxRepository;

    @Mock
    private NotificationAttachmentRepository attachmentRepository;

    @Mock
    private NotificationAttachmentStorageService attachmentStorageService;

    @Mock
    private TeacherInfoRepo teacherInfoRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private static final Long NOTIFICATION_ID = 1L;
    private static final Long PUBLISHER_ID = 10L;

    @BeforeEach
    void setUp() {

        lenient().when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> {
                    Notification notification = invocation.getArgument(0);

                    if (notification.getId() == null) {
                        notification.setId(NOTIFICATION_ID);
                    }

                    return notification;
                });
    }

    @AfterEach
    void cleanUpTransactionSynchronization() {

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.setActualTransactionActive(false);
        }
    }

    // --------------------------------------------------
    // CREATE NOTIFICATION TESTS
    // --------------------------------------------------

    @Test
    void createNotification_shouldCreateNotificationSuccessfully() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.ALL
        );

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        var response = notificationService.createNotification(
                request,
                null,
                PUBLISHER_ID,
                PublisherRole.ADMIN
        );

        assertNotNull(response);
        assertEquals(NOTIFICATION_ID, response.getId());
        assertEquals("Test Announcement", response.getTitle());
        assertEquals("Test message", response.getMessage());
        assertEquals(PublisherRole.ADMIN, response.getCreatedByRole());
        assertEquals("Admin", response.getCreatedByName());
        assertEquals(NotificationStatus.PROCESSING, response.getStatus());

        verify(notificationRepository).save(any(Notification.class));
        verify(targetRepository).saveAll(anyList());
        verify(outboxRepository).save(any());
        verify(attachmentStorageService, never()).uploadFile(any());
    }

    @Test
    void createNotification_shouldRejectInvalidTargetForTeacher() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.TEACHER
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(
                        request,
                        null,
                        PUBLISHER_ID,
                        PublisherRole.TEACHER
                )
        );

        verifyNoInteractions(notificationRepository);
        verifyNoInteractions(targetRepository);
        verifyNoInteractions(outboxRepository);
    }

    @Test
    void createNotification_shouldRejectEmptyTargets() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.STUDENT
        );

        request.setTargets(List.of());

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(
                        request,
                        null,
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );

        verifyNoInteractions(notificationRepository);
    }

    @Test
    void createNotification_shouldRejectInvalidYear() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.STUDENT
        );

        request.getTargets().get(0).setYear(5);

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(
                        request,
                        null,
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );

        verifyNoInteractions(notificationRepository);
    }

    @Test
    void createNotification_shouldRejectFiltersForAllAudience() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.ALL
        );

        request.getTargets().get(0).setBranch("CSE");

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(
                        request,
                        null,
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );

        verifyNoInteractions(notificationRepository);
    }

    @Test
    void createNotification_shouldRejectUnsupportedAttachment() {

        CreateNotificationRequest request = createRequest(
                RecipientRole.ALL
        );

        MockMultipartFile file = new MockMultipartFile(
                "attachments",
                "malicious.exe",
                "application/octet-stream",
                "test".getBytes()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(
                        request,
                        List.of(file),
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );

        verifyNoInteractions(notificationRepository);
        verifyNoInteractions(attachmentStorageService);
    }

    @Test
    void createNotification_shouldUploadAttachmentAndRegisterRollbackCleanup() {

        TransactionSynchronizationManager.initSynchronization();

        CreateNotificationRequest request = createRequest(
                RecipientRole.ALL
        );

        MockMultipartFile file = new MockMultipartFile(
                "attachments",
                "announcement.pdf",
                "application/pdf",
                "test pdf content".getBytes()
        );

        String objectKey = "notification-attachments/test.pdf";

        when(attachmentStorageService.uploadFile(file))
                .thenReturn(objectKey);

        when(attachmentRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        notificationService.createNotification(
                request,
                List.of(file),
                PUBLISHER_ID,
                PublisherRole.ADMIN
        );

        verify(attachmentStorageService).uploadFile(file);
        verify(attachmentRepository).saveAll(anyList());

        // Simulate transaction rollback.
        List<TransactionSynchronization> synchronizations =
                TransactionSynchronizationManager.getSynchronizations();

        synchronizations.forEach(sync ->
                sync.afterCompletion(
                        TransactionSynchronization.STATUS_ROLLED_BACK
                )
        );

        verify(attachmentStorageService).deleteFile(objectKey);
    }

    // --------------------------------------------------
    // GET NOTIFICATION TESTS
    // --------------------------------------------------

    @Test
    void getNotificationById_shouldReturnNotificationSuccessfully() {

        Notification notification = createPublishedNotification();

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        var response = notificationService.getNotificationById(
                NOTIFICATION_ID,
                PUBLISHER_ID,
                PublisherRole.ADMIN
        );

        assertNotNull(response);
        assertEquals(NOTIFICATION_ID, response.getId());
        assertEquals("Test Announcement", response.getTitle());
        assertEquals("Admin", response.getCreatedByName());
        assertEquals(NotificationStatus.PUBLISHED, response.getStatus());
    }

    @Test
    void getNotificationById_shouldThrowWhenNotificationDoesNotExist() {

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificationService.getNotificationById(
                        NOTIFICATION_ID,
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );
    }

    @Test
    void getNotificationById_shouldRejectUnauthorizedPublisher() {

        Notification notification = createPublishedNotification();

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.getNotificationById(
                        NOTIFICATION_ID,
                        99L,
                        PublisherRole.ADMIN
                )
        );
    }

    @Test
    void getNotificationById_shouldRejectUnpublishedNotification() {

        Notification notification = createPublishedNotification();
        notification.setStatus(NotificationStatus.PROCESSING);

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        assertThrows(
                IllegalStateException.class,
                () -> notificationService.getNotificationById(
                        NOTIFICATION_ID,
                        PUBLISHER_ID,
                        PublisherRole.ADMIN
                )
        );
    }

    // --------------------------------------------------
    // UPDATE NOTIFICATION TESTS
    // --------------------------------------------------

    @Test
    void updateNotification_shouldUpdateTitleAndMessage() {

        Notification notification = createPublishedNotification();

        UpdateNotificationRequest request = new UpdateNotificationRequest();
        request.setTitle("Updated Announcement");
        request.setMessage("Updated message");

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(targetRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        var response = notificationService.updateNotification(
                NOTIFICATION_ID,
                request,
                null,
                PUBLISHER_ID,
                PublisherRole.ADMIN
        );

        assertEquals("Updated Announcement", response.getTitle());
        assertEquals("Updated message", response.getMessage());

        verify(notificationRepository).save(notification);

        // Null attachments should retain existing attachments.
        verify(attachmentRepository, never()).deleteAll(anyList());
        verify(attachmentStorageService, never()).uploadFile(any());
    }

    @Test
    void updateNotification_shouldRejectUnauthorizedPublisher() {

        Notification notification = createPublishedNotification();

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        UpdateNotificationRequest request = new UpdateNotificationRequest();
        request.setTitle("Updated");
        request.setMessage("Updated message");

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.updateNotification(
                        NOTIFICATION_ID,
                        request,
                        null,
                        99L,
                        PublisherRole.ADMIN
                )
        );

        verify(notificationRepository, never()).save(any());
    }

    // --------------------------------------------------
    // DELETE NOTIFICATION TESTS
    // --------------------------------------------------

    @Test
    void deleteNotification_shouldDeleteNotificationSuccessfully() {

        Notification notification = createPublishedNotification();

        when(notificationRepository.findById(NOTIFICATION_ID))
                .thenReturn(Optional.of(notification));

        when(attachmentRepository.findByNotificationId(NOTIFICATION_ID))
                .thenReturn(List.of());

        notificationService.deleteNotification(
                NOTIFICATION_ID,
                PUBLISHER_ID,
                PublisherRole.ADMIN
        );

        verify(attachmentRepository).deleteAll(anyList());
        verify(notificationRepository).delete(notification);
    }

    // --------------------------------------------------
    // HELPER METHODS
    // --------------------------------------------------

    private CreateNotificationRequest createRequest(
            RecipientRole recipientRole
    ) {

        NotificationTargetRequest target = new NotificationTargetRequest();
        target.setRecipientRole(recipientRole);

        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setTitle("Test Announcement");
        request.setMessage("Test message");
        request.setCategory(NotificationCategory.GENERAL);
        request.setTargets(List.of(target));

        return request;
    }

    private Notification createPublishedNotification() {

        return Notification.builder()
                .id(NOTIFICATION_ID)
                .title("Test Announcement")
                .message("Test message")
                .category(NotificationCategory.GENERAL)
                .createdByUserId(PUBLISHER_ID)
                .createdByRole(PublisherRole.ADMIN)
                .priority(NotificationPriority.NORMAL)
                .status(NotificationStatus.PUBLISHED)
                .build();
    }
}

