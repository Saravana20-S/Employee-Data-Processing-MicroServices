package com.employeetracker.employee.batch;

import com.employeetracker.employee.entity.ImportJob;
import com.employeetracker.employee.repository.ImportJobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.BatchStatus;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobCompletionListener
        implements JobExecutionListener {

    private final ImportJobRepository
            importJobRepository;


    @Override
    public void afterJob(
            JobExecution jobExecution
    ) {

        String jobIdString =
                jobExecution
                        .getJobParameters()
                        .getString(
                                "importJobId"
                        );

        if (jobIdString == null) {
            return;
        }

        Long importJobId =
                Long.parseLong(
                        jobIdString
                );


        ImportJob importJob =
                importJobRepository
                        .findById(importJobId)
                        .orElse(null);

        if (importJob == null) {
            return;
        }


        /*
         * Batch statistics
         */

        long readCount =
                jobExecution
                        .getStepExecutions()
                        .stream()
                        .mapToLong(
                                step ->
                                        step.getReadCount()
                        )
                        .sum();


        long writeCount =
                jobExecution
                        .getStepExecutions()
                        .stream()
                        .mapToLong(
                                step ->
                                        step.getWriteCount()
                        )
                        .sum();


        long skipCount =
                jobExecution
                        .getStepExecutions()
                        .stream()
                        .mapToLong(
                                step ->
                                        step.getSkipCount()
                        )
                        .sum();


        importJob.setTotalRecords(
                (int) readCount
        );

        importJob.setSuccessfulRecords(
                (int) writeCount
        );

        importJob.setFailedRecords(
                (int) skipCount
        );


        /*
         * Determine final status
         */

        if (jobExecution.getStatus()
                == BatchStatus.COMPLETED) {

            if (skipCount == 0) {

                importJob.setStatus(
                        "SUCCESS"
                );

            } else {

                importJob.setStatus(
                        "PARTIAL"
                );
            }

        } else {

            importJob.setStatus(
                    "FAILED"
            );

            importJob.setErrorMessage(
                    "Employee import job failed"
            );
        }


        importJob.setCompletedAt(
                LocalDateTime.now()
        );


        importJobRepository.save(
                importJob
        );


        log.info(
                "Employee import completed. " +
                        "Job ID: {}, Status: {}, " +
                        "Read: {}, Written: {}, Skipped: {}",
                importJobId,
                importJob.getStatus(),
                readCount,
                writeCount,
                skipCount
        );
    }
}