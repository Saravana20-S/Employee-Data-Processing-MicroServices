package com.employeetracker.employee.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResponse {

    private Long jobId;

    private String fileName;

    private String status;

    private String message;
}