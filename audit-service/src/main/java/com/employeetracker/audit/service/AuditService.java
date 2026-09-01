package com.employeetracker.audit.service;

import com.employeetracker.audit.dto.EmployeeCreatedEvent;
import com.employeetracker.audit.entity.Audit;
import com.employeetracker.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    public Audit createAudit(
            EmployeeCreatedEvent event
    ) {

        Audit audit = Audit.builder()
                .eventType(
                        event.getEventType()
                )
                .employeeId(
                        event.getEmployeeId()
                )
                .message(
                        "Employee "
                                + event.getEmployeeId()
                                + " created in department "
                                + event.getDepartment()
                )
                .build();

        return auditRepository.save(audit);
    }
}