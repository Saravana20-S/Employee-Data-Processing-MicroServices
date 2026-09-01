package com.employeetracker.employee.repository;

import com.employeetracker.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(
            String employeeId
    );

    boolean existsByEmployeeId(
            String employeeId
    );

    boolean existsByEmail(
            String email
    );
}