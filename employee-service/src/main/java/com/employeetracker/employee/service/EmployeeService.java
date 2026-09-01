package com.employeetracker.employee.service;

import com.employeetracker.employee.dto.EmployeeRequest;
import com.employeetracker.employee.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse create(
            EmployeeRequest request
    );

    List<EmployeeResponse> getAll();

    EmployeeResponse getById(Long id);

    EmployeeResponse update(
            Long id,
            EmployeeRequest request
    );

    void delete(Long id);
}