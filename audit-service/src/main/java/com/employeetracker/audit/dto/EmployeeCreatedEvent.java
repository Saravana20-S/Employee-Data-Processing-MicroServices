package com.employeetracker.audit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreatedEvent {

    private String employeeId;

    private String name;

    private String email;

    private String department;

    private String eventType;
}