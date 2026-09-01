package com.employeetracker.audit.controller;

import com.employeetracker.audit.entity.Audit;
import com.employeetracker.audit.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditRepository auditRepository;

    @GetMapping
    public List<Audit> getAll() {

        return auditRepository.findAll();
    }

    @GetMapping("/{id}")
    public Audit getById(
            @PathVariable Long id
    ) {

        return auditRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Audit not found"
                        )
                );
    }
}