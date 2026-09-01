package com.employeetracker.employee.repository;

import com.employeetracker.employee.entity.ImportError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportErrorRepository
        extends JpaRepository<ImportError, Long> {

    List<ImportError> findByImportJobId(
            Long importJobId
    );
}