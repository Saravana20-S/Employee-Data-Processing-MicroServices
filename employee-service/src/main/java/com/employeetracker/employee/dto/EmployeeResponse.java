package com.employeetracker.employee.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;

    private String employeeId;

    private String name;

    private String email;

    private String department;

    private BigDecimal salary;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}