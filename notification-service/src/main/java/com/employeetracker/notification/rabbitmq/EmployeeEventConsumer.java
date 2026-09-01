package com.employeetracker.notification.rabbitmq;

import com.employeetracker.notification.dto.EmployeeCreatedEvent;
import com.employeetracker.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventConsumer {

    private final NotificationService
            notificationService;

    @RabbitListener(
            queues = "employee.notification.queue"
    )
    public void consume(
            EmployeeCreatedEvent event
    ) {

        log.info(
                "Received employee event: {}",
                event.getEmployeeId()
        );

        notificationService
                .createNotification(event);

        log.info(
                "Notification created for employee: {}",
                event.getEmployeeId()
        );
    }
}