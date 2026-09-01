package com.employeetracker.audit.repository;

import com.employeetracker.audit.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository
        extends JpaRepository<Audit, Long> {
}