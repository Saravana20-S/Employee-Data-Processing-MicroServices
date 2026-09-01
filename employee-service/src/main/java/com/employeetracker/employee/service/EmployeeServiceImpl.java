package com.employeetracker.employee.service;

import com.employeetracker.employee.dto.EmployeeRequest;
import com.employeetracker.employee.dto.EmployeeResponse;
import com.employeetracker.employee.entity.Employee;
import com.employeetracker.employee.exception.EmployeeNotFoundException;
import com.employeetracker.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl
        implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse create(
            EmployeeRequest request
    ) {

        if (employeeRepository.existsByEmployeeId(
                request.getEmployeeId()
        )) {

            throw new IllegalArgumentException(
                    "Employee ID already exists: "
                            + request.getEmployeeId()
            );
        }

        if (employeeRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new IllegalArgumentException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        Employee employee = Employee.builder()
                .employeeId(
                        request.getEmployeeId().trim()
                )
                .name(
                        request.getName().trim()
                )
                .email(
                        request.getEmail()
                                .trim()
                                .toLowerCase()
                )
                .department(
                        request.getDepartment().trim()
                )
                .salary(request.getSalary())
                .build();

        Employee saved =
                employeeRepository.save(employee);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {

        return employeeRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(
            Long id
    ) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse update(
            Long id,
            EmployeeRequest request
    ) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee not found with id: "
                                                + id
                                )
                        );

        if (!employee.getEmployeeId()
                .equals(request.getEmployeeId())
                && employeeRepository.existsByEmployeeId(
                request.getEmployeeId()
        )) {

            throw new IllegalArgumentException(
                    "Employee ID already exists: "
                            + request.getEmployeeId()
            );
        }

        if (!employee.getEmail()
                .equalsIgnoreCase(request.getEmail())
                && employeeRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new IllegalArgumentException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        employee.setEmployeeId(
                request.getEmployeeId().trim()
        );

        employee.setName(
                request.getName().trim()
        );

        employee.setEmail(
                request.getEmail()
                        .trim()
                        .toLowerCase()
        );

        employee.setDepartment(
                request.getDepartment().trim()
        );

        employee.setSalary(
                request.getSalary()
        );

        Employee updated =
                employeeRepository.save(employee);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee not found with id: "
                                                + id
                                )
                        );

        employeeRepository.delete(employee);
    }

    private EmployeeResponse mapToResponse(
            Employee employee
    ) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeId(employee.getEmployeeId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}