package com.employeetracker.employee.controller;

import com.employeetracker.employee.dto.ImportResponse;
import com.employeetracker.employee.entity.ImportError;
import com.employeetracker.employee.entity.ImportJob;
import com.employeetracker.employee.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/employees/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ImportResponse>
    importEmployees(
            @RequestParam("file")
            MultipartFile file
    ) {

        ImportResponse response =
                importService.importEmployees(file);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ImportJob>
    getJob(
            @PathVariable Long jobId
    ) {

        return ResponseEntity.ok(
                importService.getJob(jobId)
        );
    }

    @GetMapping("/{jobId}/errors")
    public ResponseEntity<List<ImportError>>
    getErrors(
            @PathVariable Long jobId
    ) {

        return ResponseEntity.ok(
                importService.getErrors(jobId)
        );
    }
}