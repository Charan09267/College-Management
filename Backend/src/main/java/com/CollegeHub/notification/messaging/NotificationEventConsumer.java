package com.CollegeHub.notification.messaging;

import com.CollegeHub.notification.config.NotificationRabbitMQConfig;
import com.CollegeHub.notification.event.NotificationCreatedEvent;
import com.CollegeHub.notification.service.NotificationDispatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationDispatchService dispatchService;

    @RabbitListener(
            queues = NotificationRabbitMQConfig.NOTIFICATION_QUEUE
    )
    public void consume(
            NotificationCreatedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {

        try {

            log.info(
                    "Received notification event: {}",
                    event.notificationId()
            );

            dispatchService.dispatchNotification(
                    event.notificationId()
            );

            channel.basicAck(deliveryTag, false);

        } catch (Exception exception) {

            log.error(
                    "Notification dispatch failed: {}",
                    event.notificationId(),
                    exception
            );

            // Reject and send to DLQ
            channel.basicNack(deliveryTag, false, false);
        }
    }
}