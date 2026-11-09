package com.CollegeHub.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitMQConfig {

    public static final String NOTIFICATION_EXCHANGE =
            "notification.exchange";

    public static final String NOTIFICATION_ROUTING_KEY =
            "notification.created";

    public static final String NOTIFICATION_QUEUE =
            "notification.dispatch.queue";

    public static final String NOTIFICATION_DLX =
            "notification.dlx";

    public static final String NOTIFICATION_DLQ =
            "notification.dispatch.dlq";

    public static final String DLQ_ROUTING_KEY =
            "notification.dispatch.dlq";


    // Main exchange
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(
                NOTIFICATION_EXCHANGE,
                true,
                false
        );
    }

    // Dead letter exchange
    @Bean
    public DirectExchange notificationDeadLetterExchange() {
        return new DirectExchange(
                NOTIFICATION_DLX,
                true,
                false
        );
    }

    // Main queue
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument(
                        "x-dead-letter-exchange",
                        NOTIFICATION_DLX
                )
                .withArgument(
                        "x-dead-letter-routing-key",
                        DLQ_ROUTING_KEY
                )
                .build();
    }

    // Dead letter queue
    @Bean
    public Queue notificationDeadLetterQueue() {
        return QueueBuilder.durable(NOTIFICATION_DLQ)
                .build();
    }

    // Main queue binding
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }

    // Dead letter queue binding
    @Bean
    public Binding notificationDeadLetterBinding() {
        return BindingBuilder
                .bind(notificationDeadLetterQueue())
                .to(notificationDeadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }
}
