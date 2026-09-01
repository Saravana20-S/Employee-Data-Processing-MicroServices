package com.employeetracker.notification.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String
            NOTIFICATION_QUEUE =
            "employee.notification.queue";

    @Bean
    public Queue notificationQueue() {

        return new Queue(
                NOTIFICATION_QUEUE,
                true
        );
    }
}