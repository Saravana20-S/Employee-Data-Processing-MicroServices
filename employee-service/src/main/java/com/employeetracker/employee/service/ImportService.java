package com.employeetracker.employee.service;

import com.employeetracker.employee.dto.ImportResponse;
import com.employeetracker.employee.entity.ImportError;
import com.employeetracker.employee.entity.ImportJob;
import com.employeetracker.employee.exception.ImportException;
import com.employeetracker.employee.repository.ImportErrorRepository;
import com.employeetracker.employee.repository.ImportJobRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportJobRepository importJobRepository;
    private final ImportErrorRepository importErrorRepository;

    private final JobLauncher jobLauncher;
    private final Job employeeImportJob;

    public ImportResponse importEmployees(MultipartFile file) {

        validateFile(file);

        Path tempFile = null;

        try {

            tempFile = Files.createTempFile(
                    "employee-import-",
                    ".xlsx"
            );

            Files.copy(
                    file.getInputStream(),
                    tempFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            ImportJob importJob =
                    ImportJob.builder()
                            .fileName(file.getOriginalFilename())
                            .status("STARTED")
                            .totalRecords(0)
                            .successfulRecords(0)
                            .failedRecords(0)
                            .startedAt(LocalDateTime.now())
                            .build();

            importJob =
                    importJobRepository.save(importJob);

            JobParameters parameters =
                    new JobParametersBuilder()
                            .addString(
                                    "importJobId",
                                    importJob.getId().toString()
                            )
                            .addString(
                                    "filePath",
                                    tempFile.toAbsolutePath().toString()
                            )
                            .addLong(
                                    "timestamp",
                                    System.currentTimeMillis()
                            )
                            .toJobParameters();

            jobLauncher.run(
                    employeeImportJob,
                    parameters
            );

            return ImportResponse.builder()
                    .jobId(importJob.getId())
                    .fileName(file.getOriginalFilename())
                    .status("STARTED")
                    .message(
                            "Employee import job started successfully"
                    )
                    .build();

        } catch (Exception exception) {

            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ignored) {
                }
            }

            throw new ImportException(
                    "Unable to start employee import",
                    exception
            );
        }
    }

    public ImportJob getJob(Long jobId) {

        return importJobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new ImportException(
                                "Import job not found: " + jobId
                        )
                );
    }

    public List<ImportError> getErrors(Long jobId) {

        if (!importJobRepository.existsById(jobId)) {
            throw new ImportException(
                    "Import job not found: " + jobId
            );
        }

        return importErrorRepository
                .findByImportJobId(jobId);
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ImportException(
                    "Excel file is required"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null ||
                !fileName.toLowerCase().endsWith(".xlsx")) {

            throw new ImportException(
                    "Only .xlsx files are supported"
            );
        }
    }
}
