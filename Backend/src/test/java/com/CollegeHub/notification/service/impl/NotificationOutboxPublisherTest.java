package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.config.NotificationRabbitMQConfig;
import com.CollegeHub.notification.entity.Notification;
import com.CollegeHub.notification.entity.NotificationOutbox;
import com.CollegeHub.notification.enums.OutboxStatus;
import com.CollegeHub.notification.event.NotificationCreatedEvent;
import com.CollegeHub.notification.repository.NotificationOutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationOutboxPublisherTest {

    @Mock
    private NotificationOutboxRepository outboxRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private NotificationOutbox outbox;

    @Mock
    private Notification notification;

    @InjectMocks
    private NotificationOutboxPublisher publisher;

    private static final Long NOTIFICATION_ID = 10L;
    private static final Long OUTBOX_ID = 1L;

    private void stubOutboxDetails(
            NotificationOutbox event,
            Notification notification,
            Long outboxId,
            Long notificationId
    ) {
        when(event.getId()).thenReturn(outboxId);
        when(event.getNotification()).thenReturn(notification);
        when(notification.getId()).thenReturn(notificationId);
    }

    private void stubRetryCount(NotificationOutbox event) {
        when(event.getRetryCount()).thenReturn(0);
    }

    @Test
    void publishPendingEvents_shouldFetchPendingEvents() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox));

        mockRabbitConfirm(true);

        publisher.publishPendingEvents();

        verify(outboxRepository).findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        );

        verify(rabbitTemplate).convertAndSend(
                eq(NotificationRabbitMQConfig.NOTIFICATION_EXCHANGE),
                eq(NotificationRabbitMQConfig.NOTIFICATION_ROUTING_KEY),
                any(NotificationCreatedEvent.class),
                any(CorrelationData.class)
        );
    }

    @Test
    void publishPendingEvents_shouldMarkEventAsPublishedWhenConfirmed() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox));

        mockRabbitConfirm(true);

        publisher.publishPendingEvents();

        verify(outbox).setStatus(OutboxStatus.PUBLISHED);
        verify(outbox).setPublishedAt(any(LocalDateTime.class));
        verify(outboxRepository).save(outbox);

        verify(outbox, never()).setRetryCount(anyInt());
    }

    @Test
    void publishPendingEvents_shouldIncrementRetryCountWhenRejected() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);
        stubRetryCount(outbox);

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox));

        mockRabbitConfirm(false);

        publisher.publishPendingEvents();

        verify(outbox).setRetryCount(1);
        verify(outboxRepository).save(outbox);

        verify(outbox, never()).setStatus(OutboxStatus.PUBLISHED);
        verify(outbox, never()).setPublishedAt(any(LocalDateTime.class));
    }

    @Test
    void publishPendingEvents_shouldIncrementRetryCountWhenPublishingFails() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);
        stubRetryCount(outbox);

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox));

        doThrow(new RuntimeException("RabbitMQ connection failed"))
                .when(rabbitTemplate)
                .convertAndSend(
                        anyString(),
                        anyString(),
                        any(NotificationCreatedEvent.class),
                        any(CorrelationData.class)
                );

        publisher.publishPendingEvents();

        verify(outbox).setRetryCount(1);
        verify(outboxRepository).save(outbox);

        verify(outbox, never()).setStatus(OutboxStatus.PUBLISHED);
        verify(outbox, never()).setPublishedAt(any(LocalDateTime.class));
    }

    @Test
    void publishPendingEvents_shouldIncrementRetryCountWhenMessageIsReturned() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);
        stubRetryCount(outbox);

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox));

        doAnswer(invocation -> {

            CorrelationData correlationData = invocation.getArgument(3);

            correlationData.getFuture().complete(
                    new CorrelationData.Confirm(true, null)
            );

            correlationData.setReturned(
                    new ReturnedMessage(
                            new Message(
                                    new byte[0],
                                    new MessageProperties()
                            ),
                            312,
                            "NO_ROUTE",
                            "notification.exchange",
                            "invalid.routing.key"
                    )
            );

            return null;

        }).when(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                any(NotificationCreatedEvent.class),
                any(CorrelationData.class)
        );

        publisher.publishPendingEvents();

        verify(outbox).setRetryCount(1);
        verify(outboxRepository).save(outbox);

        verify(outbox, never()).setStatus(OutboxStatus.PUBLISHED);
        verify(outbox, never()).setPublishedAt(any(LocalDateTime.class));
    }

    @Test
    void publishPendingEvents_shouldHandleEmptyPendingEvents() {

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(Collections.emptyList());

        publisher.publishPendingEvents();

        verifyNoInteractions(rabbitTemplate);
        verify(outboxRepository, never()).save(any(NotificationOutbox.class));
    }

    @Test
    void publishPendingEvents_shouldContinueProcessingWhenOneEventFails() {

        stubOutboxDetails(outbox, notification, OUTBOX_ID, NOTIFICATION_ID);
        stubRetryCount(outbox);

        NotificationOutbox secondOutbox = mock(NotificationOutbox.class);
        Notification secondNotification = mock(Notification.class);

        stubOutboxDetails(
                secondOutbox,
                secondNotification,
                2L,
                20L
        );

        when(outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING
        )).thenReturn(List.of(outbox, secondOutbox));

        AtomicInteger publishCount = new AtomicInteger();

        doAnswer(invocation -> {

            CorrelationData correlationData = invocation.getArgument(3);

            if (publishCount.getAndIncrement() == 0) {
                throw new RuntimeException("RabbitMQ failure");
            }

            correlationData.getFuture().complete(
                    new CorrelationData.Confirm(true, null)
            );

            return null;

        }).when(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                any(NotificationCreatedEvent.class),
                any(CorrelationData.class)
        );

        publisher.publishPendingEvents();

        // First event should fail and increment its retry count.
        verify(outbox).setRetryCount(1);

        // Second event should still be published successfully.
        verify(secondOutbox).setStatus(OutboxStatus.PUBLISHED);
        verify(secondOutbox).setPublishedAt(any(LocalDateTime.class));

        verify(outboxRepository, times(2)).save(any(NotificationOutbox.class));
    }

    private void mockRabbitConfirm(boolean acknowledged) {

        doAnswer(invocation -> {

            CorrelationData correlationData = invocation.getArgument(3);

            correlationData.getFuture().complete(
                    new CorrelationData.Confirm(
                            acknowledged,
                            acknowledged ? null : "Broker rejected message"
                    )
            );

            return null;

        }).when(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                any(NotificationCreatedEvent.class),
                any(CorrelationData.class)
        );
    }
}

