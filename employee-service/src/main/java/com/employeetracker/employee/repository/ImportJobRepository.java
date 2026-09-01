package com.employeetracker.employee.repository;

import com.employeetracker.employee.entity.ImportJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportJobRepository
        extends JpaRepository<ImportJob, Long> {
}