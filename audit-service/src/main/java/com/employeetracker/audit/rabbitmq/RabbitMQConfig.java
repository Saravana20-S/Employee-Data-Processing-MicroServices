package com.employeetracker.audit.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String
            AUDIT_QUEUE =
            "employee.audit.queue";

    @Bean
    public Queue auditQueue() {

        return new Queue(
                AUDIT_QUEUE,
                true
        );
    }
}