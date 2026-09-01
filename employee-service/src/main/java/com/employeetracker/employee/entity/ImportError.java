package com.employeetracker.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_errors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "import_job_id",
            nullable = false
    )
    private Long importJobId;

    @Column(
            name = "row_number",
            nullable = false
    )
    private Integer rowNumber;

    @Column(
            name = "employee_id"
    )
    private String employeeId;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "error_message",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String errorMessage;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
    }
}