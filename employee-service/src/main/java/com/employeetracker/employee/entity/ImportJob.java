package com.employeetracker.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "file_name",
            nullable = false
    )
    private String fileName;

    @Column(
            name = "status",
            nullable = false
    )
    private String status;

    @Column(
            name = "total_records",
            nullable = false
    )
    private Integer totalRecords;

    @Column(
            name = "successful_records",
            nullable = false
    )
    private Integer successfulRecords;

    @Column(
            name = "failed_records",
            nullable = false
    )
    private Integer failedRecords;

    @Column(
            name = "started_at"
    )
    private LocalDateTime startedAt;

    @Column(
            name = "completed_at"
    )
    private LocalDateTime completedAt;

    @Column(
            name = "error_message",
            columnDefinition = "TEXT"
    )
    private String errorMessage;

    @PrePersist
    protected void onCreate() {

        if (totalRecords == null) {
            totalRecords = 0;
        }

        if (successfulRecords == null) {
            successfulRecords = 0;
        }

        if (failedRecords == null) {
            failedRecords = 0;
        }

        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }
}