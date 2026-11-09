package com.CollegeHub.notification.service.impl;

import com.CollegeHub.notification.config.NotificationRabbitMQConfig;
import com.CollegeHub.notification.entity.NotificationOutbox;
import com.CollegeHub.notification.enums.OutboxStatus;
import com.CollegeHub.notification.event.NotificationCreatedEvent;
import com.CollegeHub.notification.repository.NotificationOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationOutboxPublisher {

    private final NotificationOutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final long CONFIRM_TIMEOUT_SECONDS = 10;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<NotificationOutbox> events =
                outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(
                        OutboxStatus.PENDING
                );

        for (NotificationOutbox outbox : events) {
            publishEvent(outbox);
        }
    }

    private void publishEvent(NotificationOutbox outbox) {

        Long notificationId = outbox.getNotification().getId();

        try {
            NotificationCreatedEvent event =
                    new NotificationCreatedEvent(notificationId);

            CorrelationData correlationData =
                    new CorrelationData(outbox.getId().toString());

            rabbitTemplate.convertAndSend(
                    NotificationRabbitMQConfig.NOTIFICATION_EXCHANGE,
                    NotificationRabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                    event,
                    correlationData
            );

            // Wait for RabbitMQ publisher confirmation
            CorrelationData.Confirm confirm = correlationData
                    .getFuture()
                    .get(CONFIRM_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!confirm.ack()) {
                throw new IllegalStateException(
                        "RabbitMQ rejected event: " + confirm.reason()
                );
            }

            // Check whether RabbitMQ returned an unroutable message
            if (correlationData.getReturned() != null) {
                throw new IllegalStateException(
                        "Notification event could not be routed"
                );
            }

            outbox.setStatus(OutboxStatus.PUBLISHED);
            outbox.setPublishedAt(LocalDateTime.now());

            outboxRepository.save(outbox);

            log.info(
                    "Notification event published successfully: {}",
                    notificationId
            );

        } catch (Exception exception) {

            outbox.setRetryCount(outbox.getRetryCount() + 1);
            outboxRepository.save(outbox);

            log.error(
                    "Failed to publish notification event: {}",
                    notificationId,
                    exception
            );
        }
    }
}
