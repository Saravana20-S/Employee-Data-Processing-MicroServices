package com.employeetracker.employee.controller;

import com.employeetracker.employee.dto.EmployeeRequest;
import com.employeetracker.employee.dto.EmployeeResponse;
import com.employeetracker.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(
            @Valid @RequestBody EmployeeRequest request
    ) {

        EmployeeResponse response =
                employeeService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>>
    getAll() {

        return ResponseEntity.ok(
                employeeService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse>
    getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                employeeService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse>
    update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {

        return ResponseEntity.ok(
                employeeService.update(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        employeeService.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}