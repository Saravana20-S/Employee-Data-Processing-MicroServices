package com.employeetracker.audit.rabbitmq;

import com.employeetracker.audit.dto.EmployeeCreatedEvent;
import com.employeetracker.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventConsumer {

    private final AuditService auditService;

    @RabbitListener(
            queues = "employee.audit.queue"
    )
    public void consume(
            EmployeeCreatedEvent event
    ) {

        log.info(
                "Received audit event for employee: {}",
                event.getEmployeeId()
        );

        auditService.createAudit(event);

        log.info(
                "Audit saved for employee: {}",
                event.getEmployeeId()
        );
    }
}